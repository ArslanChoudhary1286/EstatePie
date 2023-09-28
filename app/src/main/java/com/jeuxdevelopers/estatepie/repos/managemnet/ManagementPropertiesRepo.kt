package com.jeuxdevelopers.estatepie.repos.managemnet

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
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsPropertyResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ManagementPropertiesRepo {

    fun getAllReports(request: AllReportsRequest) : Flow<NetworkResult<AllReportsResponse>>

    fun getRequestByPropertyId(request: RequestsByPropertyIdRequest) : Flow<NetworkResult<RequestsByPropertyIdResponse>>

    fun getRequestsDetailById(request: Int) : Flow<NetworkResult<RequestDetailByIdResponse>>

    fun getRentFees(request: RentFeesRequest) : Flow<NetworkResult<RentFeesResponse>>

    fun getUtilityBills() : Flow<NetworkResult<MangeUtilitiesResponse>>

    fun getPropertyBills(request: PropertyBillsRequest) : Flow<NetworkResult<PropertyBillsResponse>>

    fun getResidentialProperties(request: GetResidentialRequest) : Flow<NetworkResult<ResidentialPropertiesResponse>>

    fun addResidentialProperty(request: AddResidentialRequest) : Flow<NetworkResult<AddResidentialResponse>>

    fun updateResidentialProperty(request: UpdateResidentialRequest) : Flow<NetworkResult<UpdateResidentialResponse>>

    fun deleteResidentialProperty(request: DeleteResidentialRequest) : Flow<NetworkResult<DeleteResidentialResponse>>

    fun getCommercialProperties(request: GetCommercialRequest) : Flow<NetworkResult<CommercialPropertiesResponse>>

    fun addCommercialProperty(request: AddCommercialRequest) : Flow<NetworkResult<AddCommercialResponse>>

    fun updateCommercialProperty(request: UpdateCommerecialRequest) : Flow<NetworkResult<UpdateCommercialResponse>>

    fun deleteCommercialProperty(request: DeleteCommercialRequest) : Flow<NetworkResult<DeleteCommercialResponse>>

    fun getAdsProperties(request: GetAdsRequest) : Flow<NetworkResult<AdsPropertiesResponse>>

    fun addAdsProperties(request: AddAdsRequest) : Flow<NetworkResult<AddAdsPropertyResponse>>

    fun updateAdsProperties(request: UpdateAdsRequest) : Flow<NetworkResult<UpdateAdsPropertyResponse>>

    fun deleteAdsProperties(request: DeleteAdsRequest) : Flow<NetworkResult<DeleteAdsResponse>>

    fun getPropertiesDetails(request: PropertyDetailRequest): Flow<NetworkResult<PropertiesDetailsResponse>>

    fun getAdsDetail(request: AdsDetailRequest): Flow<NetworkResult<AdsDetailResponse>>

    fun getPropertyTypes() : Flow<NetworkResult<PropertyTypeResponse>>

    fun getAllAmenities() : Flow<NetworkResult<AmenititesResponse>>

    fun getMultiListingProperties() : Flow<NetworkResult<MultiListingPropertyResponse>>

    fun getMyTenantsList(request: MyTenantsRequest) : Flow<NetworkResult<MyTenantsResponse>>

    fun getTenantsById(request: GetTenantsByIdRequest) : Flow<NetworkResult<TenantsByIdResponse>>

    fun unAssignProperty(request: UnassignPropertyRequest) : Flow<NetworkResult<UnassignPropertyResponse>>

    fun assignProperty(request: AssignPropertyRequest) : Flow<NetworkResult<AssignPropertyToTenantsResponse>>

    fun leaseReminder(request: LeaseReminderRequest) : Flow<NetworkResult<LeaseReminderResponse>>

    fun addNotesToTenants(request: AddNoteToTenantsRequest) : Flow<NetworkResult<AddNoteToTenantResponse>>

    fun deleteNote(request: DelNoteToTenantsRequest) : Flow<NetworkResult<DelNotesByIdResponse>>

    fun getPaidBills() : Flow<NetworkResult<PaidBillsResponse>>

    fun getUnPaidBills() : Flow<NetworkResult<UnPaidBillsResponse>>

    fun sendInvoice(request: SendInvoiceRequest) : Flow<NetworkResult<SendInvoiceResponse>>

    fun deleteInvoice(request: DeleteInvoiceRequest) : Flow<NetworkResult<DeleteInvoiceResponse>>

    fun getPropertiesWithBillTypes() : Flow<NetworkResult<PropertiesWithBillTypeResponse>>

    fun getPaidBillByTenantId(request: PaidBillByTenantIdRequest) : Flow<NetworkResult<PaidBillByTenantIdResponse>>

    fun getUnpaidBillByTenantId(request: UnPaidBillByTenantIdRequest) : Flow<NetworkResult<UnPaidBillByTenantIdResponse>>

    // ---------------------------------Requests Reports -------------------------------------------

    fun getMaintenanceWithFilter(request: MaintenanceWithFilterRequest) : Flow<NetworkResult<MaintenanceWithFilterResponse>>

    fun getMaintenanceWithFilterDetail(request: MaintenanceWithFilterDetailRequest) : Flow<NetworkResult<MaintenanceWithFilterDetailResponse>>

    fun changeRequestStatus(request: ChangeRequestStatusRequest) : Flow<NetworkResult<ChangeRequestStatusResponse>>

    fun postComment(request: PostCommentRequest) : Flow<NetworkResult<PostCommentResponse>>

    fun getIncidentWithFilter(request: IncidentWithFilterRequest) : Flow<NetworkResult<IncidentWithFilterResponse>>

    fun getIncidentWithFilterDetail(request: IncidentWithFilterDetailRequest) : Flow<NetworkResult<IncidentWithFilterDetailResponse>>

    fun getEventsList(request: AllEventListRequest) : Flow<NetworkResult<AllEventListResponse>>

    fun getEventsDetail(request: EventsDetailRequest) : Flow<NetworkResult<EventsDetailResponse>>

    fun deleteEvent(id: Int) : Flow<NetworkResult<DeleteEventResponse>>

    fun addNewEvent(request: AddNewEventRequest) : Flow<NetworkResult<AddNewEventResponse>>

    fun getEventsTypes() : Flow<NetworkResult<AllEventTypesResponse>>


// ---------------------------------  User Settings  ------------------------------------------------

    fun getCsvFiles() : Flow<NetworkResult<GetSampleCSVResponse>>

    fun uploadCsvFile(request: UploadCSVRequest) : Flow<NetworkResult<UploadCSVResponse>>

    fun getUserProfile() : Flow<NetworkResult<UserProfileResponse>>

    fun updateUserProfile(request: UpdateUserProfileRequest) : Flow<NetworkResult<UpdateUserProfileResponse>>

    fun changePassword(request: ChangePasswordRequest) : Flow<NetworkResult<ChangePasswordResponse>>

    fun getAllNotifications() : Flow<NetworkResult<GetNotificationsResponse>>

    fun readNotification(request: ReadNotificationRequest) : Flow<NetworkResult<ReadNotificationResponse>>

    fun getNotificationCount() : Flow<NetworkResult<NotificationCountResponse>>

    fun pushNotificationToggle() : Flow<NetworkResult<PushNotificationToggleResponse>>

    fun emailNotificationToggle() : Flow<NetworkResult<EmailNotificationToggleResponse>>

}