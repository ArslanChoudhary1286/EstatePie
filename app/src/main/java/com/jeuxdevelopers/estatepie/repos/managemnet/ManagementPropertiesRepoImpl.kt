package com.jeuxdevelopers.estatepie.repos.managemnet

import com.jeuxdevelopers.estatepie.network.BaseApiResponse
import com.jeuxdevelopers.estatepie.network.RemoteDataSource
import com.jeuxdevelopers.estatepie.network.requests.explore.AdsDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.management.billing.DeleteInvoiceRequest
import com.jeuxdevelopers.estatepie.network.requests.management.billing.PaidBillByTenantIdRequest
import com.jeuxdevelopers.estatepie.network.requests.management.billing.SendInvoiceRequest
import com.jeuxdevelopers.estatepie.network.requests.management.billing.UnPaidBillByTenantIdRequest
import com.jeuxdevelopers.estatepie.network.requests.management.csv.UploadCSVRequest
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.AllReportsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.PropertyBillsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.RentFeesRequest
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.RequestsByPropertyIdRequest
import com.jeuxdevelopers.estatepie.network.requests.management.notification.ReadNotificationRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.*
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.*
import com.jeuxdevelopers.estatepie.network.requests.management.settings.ChangePasswordRequest
import com.jeuxdevelopers.estatepie.network.requests.management.settings.UpdateUserProfileRequest
import com.jeuxdevelopers.estatepie.network.requests.management.tenants.*
import com.jeuxdevelopers.estatepie.network.requests.tenant.properties.PropertyDetailRequest
import com.jeuxdevelopers.estatepie.network.responses.management.billing.*
import com.jeuxdevelopers.estatepie.network.responses.management.csv.GetSampleCSVResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.*
import com.jeuxdevelopers.estatepie.network.responses.management.properties.*
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.*
import com.jeuxdevelopers.estatepie.network.responses.management.settings.ChangePasswordResponse
import com.jeuxdevelopers.estatepie.network.responses.management.settings.UpdateUserProfileResponse
import com.jeuxdevelopers.estatepie.network.responses.management.settings.UserProfileResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.*
import com.jeuxdevelopers.estatepie.network.responses.notification.*
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ManagementPropertiesRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) :ManagementPropertiesRepo, BaseApiResponse() {

    override fun getAllReports(request: AllReportsRequest): Flow<NetworkResult<AllReportsResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getAllReports(request) })
        }.flowOn(Dispatchers.IO)

    override fun getRequestByPropertyId(request: RequestsByPropertyIdRequest): Flow<NetworkResult<RequestsByPropertyIdResponse>> =
    flow {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.getRequestByPropertyId(request) })
    }.flowOn(Dispatchers.IO)

    override fun getRequestsDetailById(request: Int): Flow<NetworkResult<RequestDetailByIdResponse>> =
    flow {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.getRequestsDetailById(request) })
    }.flowOn(Dispatchers.IO)

    override fun getRentFees(request: RentFeesRequest): Flow<NetworkResult<RentFeesResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getRentFees(request) })
        }.flowOn(Dispatchers.IO)

    override fun getUtilityBills(): Flow<NetworkResult<MangeUtilitiesResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getUtilityBills() })
        }.flowOn(Dispatchers.IO)

    override fun getPropertyBills(request: PropertyBillsRequest): Flow<NetworkResult<PropertyBillsResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPropertyBills(request) })
        }.flowOn(Dispatchers.IO)

    override fun getResidentialProperties(request: GetResidentialRequest): Flow<NetworkResult<ResidentialPropertiesResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getResidentialProperties(request) })
        }.flowOn(Dispatchers.IO)

    override fun addResidentialProperty(request: AddResidentialRequest): Flow<NetworkResult<AddResidentialResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addResidentialProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun updateResidentialProperty(request: UpdateResidentialRequest): Flow<NetworkResult<UpdateResidentialResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.updateResidentialProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteResidentialProperty(request: DeleteResidentialRequest): Flow<NetworkResult<DeleteResidentialResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteResidentialProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun getCommercialProperties(request: GetCommercialRequest): Flow<NetworkResult<CommercialPropertiesResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getCommercialProperties(request) })
        }.flowOn(Dispatchers.IO)

    override fun addCommercialProperty(request: AddCommercialRequest): Flow<NetworkResult<AddCommercialResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addCommercialProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun updateCommercialProperty(request: UpdateCommerecialRequest): Flow<NetworkResult<UpdateCommercialResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.updateCommercialProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteCommercialProperty(request: DeleteCommercialRequest): Flow<NetworkResult<DeleteCommercialResponse>>   =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteCommercialProperty(request) })
        }.flowOn(Dispatchers.IO)


    override fun getAdsProperties(request: GetAdsRequest): Flow<NetworkResult<AdsPropertiesResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getAdsProperties(request) })
        }.flowOn(Dispatchers.IO)


    override fun addAdsProperties(request: AddAdsRequest): Flow<NetworkResult<AddAdsPropertyResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addAdsProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun updateAdsProperties(request: UpdateAdsRequest): Flow<NetworkResult<UpdateAdsPropertyResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.updateAdsProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteAdsProperties(request: DeleteAdsRequest): Flow<NetworkResult<DeleteAdsResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteAdsProperty(request) })
        }.flowOn(Dispatchers.IO)

    override fun getPropertiesDetails(request: PropertyDetailRequest): Flow<NetworkResult<PropertiesDetailsResponse>>   =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPropertyDetails(request) })
        }.flowOn(Dispatchers.IO)

    override fun getAdsDetail(request: AdsDetailRequest): Flow<NetworkResult<AdsDetailResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getAdsDetail(request) })
        }.flowOn(Dispatchers.IO)


    override fun getPropertyTypes(): Flow<NetworkResult<PropertyTypeResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPropertyTypes() })
        }.flowOn(Dispatchers.IO)

    override fun getAllAmenities(): Flow<NetworkResult<AmenititesResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getAllAmenities() })
        }.flowOn(Dispatchers.IO)

    override fun getMultiListingProperties(): Flow<NetworkResult<MultiListingPropertyResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getMultiListingProperties() })
        }.flowOn(Dispatchers.IO)

    override fun getMyTenantsList(request: MyTenantsRequest): Flow<NetworkResult<MyTenantsResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getMyTenants(request) })
        }.flowOn(Dispatchers.IO)

    override fun getTenantsById(request: GetTenantsByIdRequest): Flow<NetworkResult<TenantsByIdResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getTenantsById(request) })
        }.flowOn(Dispatchers.IO)

    override fun unAssignProperty(request: UnassignPropertyRequest): Flow<NetworkResult<UnassignPropertyResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.unAssignPropertyToTenants(request) })
        }.flowOn(Dispatchers.IO)

    override fun assignProperty(request: AssignPropertyRequest): Flow<NetworkResult<AssignPropertyToTenantsResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.assignPropertyToTenants(request) })
        }.flowOn(Dispatchers.IO)


    override fun leaseReminder(request: LeaseReminderRequest): Flow<NetworkResult<LeaseReminderResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.leaseReminder(request) })
        }.flowOn(Dispatchers.IO)

    override fun addNotesToTenants(request: AddNoteToTenantsRequest): Flow<NetworkResult<AddNoteToTenantResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addNoteToTenants(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteNote(request: DelNoteToTenantsRequest): Flow<NetworkResult<DelNotesByIdResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteNote(request) })
        }.flowOn(Dispatchers.IO)

    override fun getPaidBills(): Flow<NetworkResult<PaidBillsResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPaidBills() })
        }.flowOn(Dispatchers.IO)

    override fun getUnPaidBills(): Flow<NetworkResult<UnPaidBillsResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getUnPaidBills() })
        }.flowOn(Dispatchers.IO)

    override fun sendInvoice(request: SendInvoiceRequest): Flow<NetworkResult<SendInvoiceResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.sendInvoice(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteInvoice(request: DeleteInvoiceRequest): Flow<NetworkResult<DeleteInvoiceResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteInvoice(request) })
        }.flowOn(Dispatchers.IO)

    override fun getPropertiesWithBillTypes(): Flow<NetworkResult<PropertiesWithBillTypeResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPropertiesWithBillTypes() })
        }.flowOn(Dispatchers.IO)

    override fun getPaidBillByTenantId(request: PaidBillByTenantIdRequest): Flow<NetworkResult<PaidBillByTenantIdResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPaidBillByTenantId(request) })
        }.flowOn(Dispatchers.IO)

    override fun getUnpaidBillByTenantId(request: UnPaidBillByTenantIdRequest): Flow<NetworkResult<UnPaidBillByTenantIdResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getUnpaidBillByTenantId(request) })
        }.flowOn(Dispatchers.IO)

    override fun getMaintenanceWithFilter(request: MaintenanceWithFilterRequest): Flow<NetworkResult<MaintenanceWithFilterResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getMaintenanceWithFilter(request) })
        }.flowOn(Dispatchers.IO)

    override fun getMaintenanceWithFilterDetail(request: MaintenanceWithFilterDetailRequest): Flow<NetworkResult<MaintenanceWithFilterDetailResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getMaintenanceWithFilterDetail(request) })
        }.flowOn(Dispatchers.IO)

    override fun changeRequestStatus(request: ChangeRequestStatusRequest): Flow<NetworkResult<ChangeRequestStatusResponse>> =
    flow {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.changeRequestStatus(request) })
    }.flowOn(Dispatchers.IO)

    override fun postComment(request: PostCommentRequest): Flow<NetworkResult<PostCommentResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.postComment(request) })
        }.flowOn(Dispatchers.IO)

    override fun getIncidentWithFilter(request: IncidentWithFilterRequest): Flow<NetworkResult<IncidentWithFilterResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getIncidentWithFilter(request) })
        }.flowOn(Dispatchers.IO)

    override fun getIncidentWithFilterDetail(request: IncidentWithFilterDetailRequest): Flow<NetworkResult<IncidentWithFilterDetailResponse>> =
    flow {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.getIncidentWithFilterDetail(request) })
    }.flowOn(Dispatchers.IO)

    override fun getEventsList(request: AllEventListRequest): Flow<NetworkResult<AllEventListResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getEventsList(request) })
        }.flowOn(Dispatchers.IO)

    override fun getEventsDetail(request: EventsDetailRequest): Flow<NetworkResult<EventsDetailResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getEventsDetail(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteEvent(id: Int): Flow<NetworkResult<DeleteEventResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteEvent(id) })
        }.flowOn(Dispatchers.IO)

    override fun addNewEvent(request: AddNewEventRequest): Flow<NetworkResult<AddNewEventResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addNewEvent(request) })
        }.flowOn(Dispatchers.IO)

    override fun getEventsTypes(): Flow<NetworkResult<AllEventTypesResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getEventsTypes() })
        }.flowOn(Dispatchers.IO)

    override fun getCsvFiles(): Flow<NetworkResult<GetSampleCSVResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getSampleCSV() })
        }.flowOn(Dispatchers.IO)

    override fun uploadCsvFile(request: UploadCSVRequest): Flow<NetworkResult<UploadCSVResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.uploadCSV(request) })
        }.flowOn(Dispatchers.IO)

// ---------------------------------  User Settings  ------------------------------------------------

    override fun getUserProfile(): Flow<NetworkResult<UserProfileResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getUserProfile() })
        }.flowOn(Dispatchers.IO)

    override fun updateUserProfile(request: UpdateUserProfileRequest): Flow<NetworkResult<UpdateUserProfileResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.updateUserProfile(request) })
        }.flowOn(Dispatchers.IO)

    override fun changePassword(request: ChangePasswordRequest): Flow<NetworkResult<ChangePasswordResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.changePassword(request) })
        }.flowOn(Dispatchers.IO)

    override fun getAllNotifications(): Flow<NetworkResult<GetNotificationsResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getAllNotifications() })
        }.flowOn(Dispatchers.IO)

    override fun readNotification(request: ReadNotificationRequest): Flow<NetworkResult<ReadNotificationResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.readNotification(request) })
        }.flowOn(Dispatchers.IO)

    override fun getNotificationCount(): Flow<NetworkResult<NotificationCountResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getNotificationCount() })
        }.flowOn(Dispatchers.IO)

    override fun pushNotificationToggle(): Flow<NetworkResult<PushNotificationToggleResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.pushNotificationToggle() })
        }.flowOn(Dispatchers.IO)

    override fun emailNotificationToggle(): Flow<NetworkResult<EmailNotificationToggleResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.emailNotificationToggle() })
        }.flowOn(Dispatchers.IO)


}