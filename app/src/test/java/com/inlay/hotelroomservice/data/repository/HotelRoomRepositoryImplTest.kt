package com.inlay.hotelroomservice.data.repository


import com.inlay.hotelroomservice.CoroutineTestRule
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemStaysEntity
import com.inlay.hotelroomservice.data.mapping.toEntity
import com.inlay.hotelroomservice.data.mapping.toUiItem
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import com.inlay.hotelroomservice.domain.local.LocalDataSource
import com.inlay.hotelroomservice.domain.remote.RemoteDataSource
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import retrofit2.Response

internal class HotelRoomRepositoryImplTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val remoteDataSource = mockk<RemoteDataSource>(relaxed = true)
    private val localDataSource = mockk<LocalDataSource>(relaxed = true)

    private lateinit var repository: HotelRoomRepositoryImpl

    @Before
    fun setUp() {
        repository = HotelRoomRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getSearchLocationRepo with unsuccessful response`() = runTest {
        val searchLocationResponseMock = mockk<Response<SearchLocationModel>>(relaxed = true)

        coEvery { remoteDataSource.getSearchLocationRepo(any()) } returns searchLocationResponseMock
        every { searchLocationResponseMock.isSuccessful } returns false

        val result = repository.getSearchLocationRepo("location")

        assertEquals(listOf<SearchLocationsUiModel>(), result)
    }

    @Test
    fun `getSearchLocationRepo with successful response`() = runTest {
        val listOfData = listOf<SearchLocationsUiModel>()

        val searchLocationResponseMock = mockk<Response<SearchLocationModel>>(relaxed = true)
        val dataModelMock = mockk<SearchLocationModel>(relaxed = true)

        coEvery { remoteDataSource.getSearchLocationRepo(any()) } returns searchLocationResponseMock
        every { searchLocationResponseMock.isSuccessful } returns false
        every { searchLocationResponseMock.body() } returns dataModelMock

        val result = repository.getSearchLocationRepo("location")

        assertEquals(listOfData, result)
    }

    @Test
    fun `getHotelRepo if user is online`() = runTest {
        val listOfData = listOf<HotelsItemUiModel>()

        val hotelsDataMock = mockk<Response<HotelsModel>>(relaxed = true)
        val daraModel = mockk<HotelsModel>(relaxed = true)

        coEvery {
            remoteDataSource.getHotelsRepo(
                any(), any(), any(), any()
            )
        } returns hotelsDataMock

        every { hotelsDataMock.isSuccessful } returns true
        every { hotelsDataMock.body() } returns daraModel

        val result = repository.getHotelRepo(
            true, "", "", "", ""
        )

        assertEquals(listOfData, result)
    }

    @Test
    fun `getHotelRepo if user is offline`() = runTest {
        val uiData = HotelsItemUiModel("0", "", "", "", "", "", listOf())
        val listOfData = listOf(uiData)

        val hotelItemEntityMock = mockk<HotelsItemEntity>(relaxed = true)
        val listOfLocalData = listOf(hotelItemEntityMock)
        val localDataFlow = flowOf(listOfLocalData)

        coEvery { localDataSource.fetchRepo() } returns localDataFlow

        val result = repository.getHotelRepo(false, "", "", "", "")

        assertEquals(listOfData, result)
    }

    @Test
    fun `getHotelDetails with successful response`() = runTest {
        val uiData = HotelDetailsUiModel(
            "",
            "0.0",
            "0",
            "",
            "",
            "",
            listOf(),
            "",
            listOf(),
            listOf(),
            "",
            listOf(),
            listOf(),
            0.0,
            0.0
        )

        val detailsDataResponseMock = mockk<Response<HotelDetailsModel>>(relaxed = true)
        val detailsDataMock = mockk<HotelDetailsModel>(relaxed = true)

        coEvery {
            remoteDataSource.getHotelDetailsRepo(
                any(), any(), any(), any()
            )
        } returns detailsDataResponseMock
        every { detailsDataResponseMock.isSuccessful } returns true
        every { detailsDataResponseMock.body() } returns detailsDataMock

        val result = repository.getHotelDetails("", "", "", "")

        assertEquals(uiData, result)
    }

    @Test
    fun `getHotelDetails with unsuccessful response`() = runTest {
        val uiEmptyData = HotelDetailsUiModel(
            "",
            "",
            "",
            "",
            "",
            "",
            listOf(),
            "",
            listOf(),
            listOf(),
            "",
            listOf(),
            listOf(),
            0.0,
            0.0
        )

        val detailsDataResponseMock = mockk<Response<HotelDetailsModel>>(relaxed = true)

        coEvery {
            remoteDataSource.getHotelDetailsRepo(
                any(), any(), any(), any()
            )
        } returns detailsDataResponseMock
        every { detailsDataResponseMock.isSuccessful } returns false

        val result = repository.getHotelDetails("", "", "", "")

        assertEquals(uiEmptyData, result)
    }

    @Test
    fun `getStaysRepo with user isLogged and isOnline`() = runTest {
        val dataFlow = mockk<Flow<List<HotelsItemUiModel?>>>(relaxed = true)

        coEvery { remoteDataSource.getStaysRepo() } returns dataFlow

        val value = repository.getStaysRepo(isOnline = true, isLogged = true)

        assertEquals(dataFlow, value)
    }

    @Test
    fun `getStaysRepo with user !isLogged and !isOnline`() = runTest {
        val entityItemMock = mockk<HotelsItemStaysEntity>(relaxed = true)
        val entityList = listOf(entityItemMock)
        val localDataFlow = flowOf(entityList)

        val rFlow = localDataFlow.map { list -> list.map { it.toUiItem() } }

        coEvery { localDataSource.fetchStaysRepo() } returns localDataFlow

        val result = repository.getStaysRepo(isOnline = false, isLogged = false)

        assertEquals(rFlow.first(), result.first())
    }

    @Test
    fun `addStaysRepo with user isOnline and isLogged`() = runTest {
        val addStaysRemoteMock = mockk<(HotelsItemUiModel) -> Unit>(relaxed = true)
        val addStaysLocalMock = mockk<(HotelsItemStaysEntity) -> Unit>(relaxed = true)

        val hotelItemMock = mockk<HotelsItemUiModel>(relaxed = true)
        val hotelItemEntityMock = mockk<HotelsItemStaysEntity>(relaxed = true)
        every { hotelItemMock.id } returns "0"
        every { hotelItemEntityMock.id } returns 0

        coEvery { remoteDataSource.addStaysRepo(any()) } returns addStaysRemoteMock.invoke(
            hotelItemMock
        )
        coEvery { localDataSource.insertStayRepo(any()) } returns addStaysLocalMock.invoke(
            hotelItemEntityMock
        )

        repository.addStaysRepo(hotelItemMock, isOnline = true, isLogged = true)

        verify { addStaysRemoteMock.invoke(hotelItemMock) }
        verify { addStaysLocalMock.invoke(hotelItemEntityMock) }
    }

    @Test
    fun `addStaysRepo with user !isOnline and !isLogged`() = runTest {
        val addStaysLocalMock = mockk<(HotelsItemStaysEntity) -> Unit>(relaxed = true)

        val hotelItemMock = mockk<HotelsItemUiModel>(relaxed = true)
        val hotelItemEntityMock = mockk<HotelsItemStaysEntity>(relaxed = true)
        every { hotelItemMock.id } returns "0"
        every { hotelItemEntityMock.id } returns 0


        coEvery { localDataSource.insertStayRepo(any()) } returns addStaysLocalMock.invoke(
            hotelItemEntityMock
        )

        repository.addStaysRepo(hotelItemMock, isOnline = false, isLogged = false)

        verify { addStaysLocalMock.invoke(hotelItemEntityMock) }
    }

    @Test
    fun `removeStaysRepo with user isOnline and isLogged`() = runTest {
        val removeStayRemoteMock = mockk<(HotelsItemUiModel) -> Unit>(relaxed = true)
        val removeStayLocalMock = mockk<(HotelsItemStaysEntity) -> Unit>(relaxed = true)

        val hotelsItemMock = mockk<HotelsItemUiModel>(relaxed = true)
        val hotelsItemEntityMock = mockk<HotelsItemStaysEntity>(relaxed = true)

        every { hotelsItemMock.id } returns "0"
        every { hotelsItemEntityMock.id } returns 0

        coEvery { remoteDataSource.removeStaysRepo(any()) } returns removeStayRemoteMock.invoke(
            hotelsItemMock
        )
        coEvery { localDataSource.deleteStayRepo(any()) } returns removeStayLocalMock.invoke(
            hotelsItemEntityMock
        )

        repository.removeStaysRepo(hotelsItemMock, isOnline = true, isLogged = true)

        verify { removeStayRemoteMock.invoke(hotelsItemMock) }
        verify { removeStayLocalMock.invoke(hotelsItemEntityMock) }
    }

    @Test
    fun `removeStaysRepo with user !isOnline and !isLogged`() = runTest {
        val removeStayLocalMock = mockk<(HotelsItemStaysEntity) -> Unit>(relaxed = true)

        val hotelsItemMock = mockk<HotelsItemUiModel>(relaxed = true)
        val hotelsItemEntityMock = mockk<HotelsItemStaysEntity>(relaxed = true)

        every { hotelsItemMock.id } returns "0"
        every { hotelsItemEntityMock.id } returns 0

        coEvery { localDataSource.deleteStayRepo(any()) } returns removeStayLocalMock.invoke(
            hotelsItemEntityMock
        )

        repository.removeStaysRepo(hotelsItemMock, isOnline = false, isLogged = false)

        verify { removeStayLocalMock.invoke(hotelsItemEntityMock) }
    }

    @Test
    fun saveNightModeState() = runTest {
        val saveNightModeStateMock = mockk<(Int) -> Unit>(relaxed = true)

        coEvery { localDataSource.saveNightModeState(any()) } returns saveNightModeStateMock.invoke(
            0
        )

        repository.saveNightModeState(0)

        verify { saveNightModeStateMock.invoke(0) }
    }

    @Test
    fun saveNotificationsState() = runTest {
        val saveNotificationsStateMock = mockk<(Boolean) -> Unit>(relaxed = true)

        coEvery { localDataSource.saveNotificationsState(any()) } returns saveNotificationsStateMock.invoke(
            true
        )

        repository.saveNotificationsState(true)

        verify { saveNotificationsStateMock.invoke(true) }
    }

    @Test
    fun getNightModeState() = runTest {
        val resultFlow = mockk<Flow<Int>>(relaxed = true)

        coEvery { localDataSource.getNightModeState() } returns resultFlow

        val result = repository.getNightModeState()

        assertEquals(resultFlow, result)
    }

    @Test
    fun getNotificationsState() = runTest {
        val resultFlow = mockk<Flow<Boolean>>(relaxed = true)

        coEvery { localDataSource.getNotificationsState() } returns resultFlow

        val result = repository.getNotificationsState()

        assertEquals(resultFlow, result)
    }
}