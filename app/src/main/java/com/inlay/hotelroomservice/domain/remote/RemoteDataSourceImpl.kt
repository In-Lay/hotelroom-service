package com.inlay.hotelroomservice.domain.remote

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.data.remote.apiservice.HotelRoomApiService
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import retrofit2.Response

class RemoteDataSourceImpl(
    private val hotelRoomApiService: HotelRoomApiService, private val database: FirebaseDatabase
) : RemoteDataSource {
    override suspend fun getSearchLocationRepo(location: String): Response<SearchLocationModel> {
        return hotelRoomApiService.getSearchLocalData(location)
    }

    override suspend fun getHotelsRepo(
        geoId: String, checkInDate: String, checkOutDate: String, currencyCode: String
    ): Response<HotelsModel> {
        return hotelRoomApiService.getHotelsData(geoId, checkInDate, checkOutDate, currencyCode)
    }

    override suspend fun getStaysRepo(): Flow<List<HotelsItemUiModel?>> {
        val currentUser = Firebase.auth.currentUser
        val databaseReference = database.reference.child("userStays").child(currentUser?.uid!!)
        val hotelsItemListFlow = MutableStateFlow<List<HotelsItemUiModel?>>(listOf())

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hotelsListItem = snapshot.children.map {
                    it.getValue(HotelsItemUiModel::class.java)
                }
                hotelsItemListFlow.value = hotelsListItem
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        databaseReference.addValueEventListener(valueEventListener)

        return hotelsItemListFlow
    }

    override suspend fun addStaysRepo(hotelsItem: HotelsItemUiModel) {
        val currentUser = Firebase.auth.currentUser
        val databaseReference = database.reference.child("userStays").child(currentUser?.uid!!)
        val hotelsItemReference = databaseReference.orderByChild("id").equalTo(hotelsItem.id)

        hotelsItemReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) databaseReference.push().setValue(hotelsItem)
                    .addOnSuccessListener {
                        hotelsItemReference.removeEventListener(this)
                    }
                //Updates item
//                else {
//                    snapshot.children.forEach {
//                        it.ref.setValue(hotelsItem)
//                    }
//                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    override suspend fun removeStaysRepo(hotelsItem: HotelsItemUiModel) {
        val currentUser = Firebase.auth.currentUser
        val databaseReference = database.reference.child("userStays").child(currentUser?.uid!!)
        val itemId = findItemIdToDelete(currentUser, databaseReference, hotelsItem)

        if (itemId.isNullOrEmpty()) {
            Log.d("firebaseLog", "removeStaysRepo: itemId.isNullOrEmpty: $itemId")
        } else databaseReference.child(itemId)
            .removeValue()
    }

    override suspend fun findItemIdToDelete(
        user: FirebaseUser?, databaseReference: DatabaseReference, hotelsItem: HotelsItemUiModel
    ): String? {
        val querySnapshot =
            databaseReference.orderByChild("id").equalTo(hotelsItem.id).limitToFirst(1).get()
                .await()

        return querySnapshot.children.first().key
    }

    override suspend fun getHotelDetailsRepo(
        id: String, checkInDate: String, checkOutDate: String, currencyCode: String
    ): Response<HotelDetailsModel> {
        return hotelRoomApiService.getHotelDetailsData(id, checkInDate, checkOutDate, currencyCode)
    }
}