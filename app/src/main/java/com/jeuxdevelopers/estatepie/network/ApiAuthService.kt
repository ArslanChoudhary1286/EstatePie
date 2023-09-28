package com.jeuxdevelopers.estatepie.network

import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.PostCommentRequest
import com.jeuxdevelopers.estatepie.network.requests.management.settings.ChangePasswordRequest
import com.jeuxdevelopers.estatepie.network.requests.management.tenants.*
import com.jeuxdevelopers.estatepie.network.requests.plans.CreateCustomerRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.MakeSubscriptionsRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.PayBillRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.UpdateCustomerRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.billing.AddPaymentScheduleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.billing.UpdatePaymentScheduleRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.DeleteAccountResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.MarkToFavoriteResponse
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
import com.jeuxdevelopers.estatepie.network.responses.others.TermsConditionResponse
import com.jeuxdevelopers.estatepie.network.responses.plans.*
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.*
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.*
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.FavoritesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.AddVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.DeleteVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.GetVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.UpdateVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.AllAssignPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.SendReportResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiAuthService {

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/delete_account")
    suspend fun deleteAccount(
        @Query("device_type") device_type: String,
        @Query("device_token") device_token: String,
    ): Response<DeleteAccountResponse>

    // property apis

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/mark_as_favourite/{id}")
    suspend fun markToFavorite(
        @Path("id") id : String
    ): Response<MarkToFavoriteResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_all_favorites")
    suspend fun getAllFavorites(
    ): Response<FavoritesResponse>

    // report_type: [All, Maintenance, Incident, Events]

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_rent_fees")
    suspend fun getRentFees(
        @Query("keyword") keyword: String,
        @Query("months") months: String
    ): Response<RentFeesResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_maintenance_reports_with_filters")
    suspend fun getAllReports(
        @Query("page") page: String,
        @Query("report_type") reportType: String,
        @Query("date") date: String
    ): Response<AllReportsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_request_by_property_id/{id}")
    suspend fun getRequestsByPropertyId(
        @Path("id") id: String,
        @Query("page") page: String
    ): Response<RequestsByPropertyIdResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_request_detail_by_id/{id}")
    suspend fun getRequestsDetailById(
        @Path("id") id: Int
    ): Response<RequestDetailByIdResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_utilities_data")
    suspend fun getUtilityBills(
    ): Response<MangeUtilitiesResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_property_bills/{id}")
    suspend fun getPropertyBills(
        @Path("id") id: String,
        @Query("status") status: String
    ): Response<PropertyBillsResponse>

    @Multipart
    @POST("/api/v1/upload_csv")
    suspend fun uploadCSV(
        @Part("id") id: RequestBody,
        @Part file:MultipartBody.Part,
    ): Response<UploadCSVResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_sample_csv")
    suspend fun getSampleCSV(
    ): Response<GetSampleCSVResponse>

    //------------------------------------- property ---------------------------------

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/residential_properties")
    suspend fun getResidentialProperties(
        @Query("sort") sort: String,
        @Query("keyword") keyword: String,
        @Query("search") search: String,
        @Query("page") page: String
    ): Response<ResidentialPropertiesResponse>

    @Multipart
    @POST("/api/v1/residential_properties")
    suspend fun addResidentialProperty(
        @Part("name") name: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("apt_number") apt_number: RequestBody,
        @Part("number_range_from") number_range_from: RequestBody,
        @Part("number_range_to") number_range_to: RequestBody,
        @Part("range_alpha_from") range_alpha_from: RequestBody,
        @Part("range_alpha_to") range_alpha_to: RequestBody,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("status") status: RequestBody,
        @Part images:List<MultipartBody.Part>
    ): Response<AddResidentialResponse>

    @Multipart
    @POST("/api/v1/residential_properties/{id}")
    suspend fun updateResidentialProperty(
        @Path("id") id : Int,
        @Part("name") name: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("unit") unit: RequestBody,
//        @Part("number_range_from") number_range_from: RequestBody,
//        @Part("number_range_to") number_range_to: RequestBody,
//        @Part("range_alpha_from") range_alpha_from: RequestBody,
//        @Part("range_alpha_to") range_alpha_to: RequestBody,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("_method") method: RequestBody,
//        @Part("status") status: RequestBody,
        @Part images:List<MultipartBody.Part>
    ): Response<UpdateResidentialResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/residential_properties/{id}")
    suspend fun deleteResidentialProperty(
        @Path("id") id : String
    ): Response<DeleteResidentialResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/commercial_properties")
    suspend fun getCommercialProperties(
        @Query("sort") sort: String,
        @Query("keyword") keyword: String,
        @Query("search") search: String,
        @Query("page") page: String
    ): Response<CommercialPropertiesResponse>

    @Multipart
    @POST("/api/v1/commercial_properties")
    suspend fun addCommercialProperty(
        @Part("name") name: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("apt_number") apt_number: RequestBody,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("status") status: RequestBody,
        @Part images:List<MultipartBody.Part>
    ): Response<AddCommercialResponse>

    @Multipart
    @POST("/api/v1/commercial_properties/{id}")
    suspend fun updateCommercialProperty(
        @Path("id") id : Int,
        @Part("name") name: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("unit") unit: RequestBody,
        @Part("apt_number") apt: RequestBody,
//        @Part("number_range_from") number_range_from: RequestBody,
//        @Part("number_range_to") number_range_to: RequestBody,
//        @Part("range_alpha_from") range_alpha_from: RequestBody,
//        @Part("range_alpha_to") range_alpha_to: RequestBody,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("_method") method: RequestBody,
//        @Part("status") status: RequestBody,
        @Part images:List<MultipartBody.Part>
    ): Response<UpdateCommercialResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/commercial_properties/{id}")
    suspend fun deleteCommercialProperty(
        @Path("id") id : String
    ): Response<DeleteCommercialResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/ads_properties")
    suspend fun getAdsProperties(
        @Query("sort") sort: String,
        @Query("keyword") keyword: String,
        @Query("search") search: String,
        @Query("page") page: String
    ): Response<AdsPropertiesResponse>

    @Multipart
    @POST("/api/v1/ads_properties")
    suspend fun addAdsProperty(
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("purpose") purpose: RequestBody,
        @Part("unit") unit: RequestBody,
        @Part("size") size: RequestBody,
        @Part("broker") broker: RequestBody,
        @Part("price") price: RequestBody,
        @Part("address") address: RequestBody,
        @Part("description") description: RequestBody,
        @Part("lease") lease: RequestBody,
        @Part("beds") beds: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("multiunits") multiunits: RequestBody,
        @Part("available") available: RequestBody,
        @Part images:List<MultipartBody.Part>,
        @Part("amenities") amenities: RequestBody,
        @Part("property_type") property_type: RequestBody,
        @Part("lease_terms") lease_terms: RequestBody,
        @Part("smoking") smoking: RequestBody,
        @Part("pets") pets: RequestBody,
        @Part("bathroom") bathroom: RequestBody

    ): Response<AddAdsPropertyResponse>

    @Multipart
    @POST("/api/v1/ads_properties/{id}")
    suspend fun updateAdsProperty(
        @Path("id") id : Int,
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("purpose") purpose: RequestBody,
        @Part("unit") unit: RequestBody,
        @Part("size") size: RequestBody,
        @Part("broker") broker: RequestBody,
        @Part("price") price: RequestBody,
        @Part("address") address: RequestBody,
        @Part("description") description: RequestBody,
        @Part("lease") lease: RequestBody,
        @Part("beds") beds: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("multiunits") multiunits: RequestBody,
        @Part("available") available: RequestBody,
        @Part images:List<MultipartBody.Part>,
        @Part("amenities") amenities: RequestBody,
        @Part("property_type") property_type: RequestBody,
        @Part("lease_terms") lease_terms: RequestBody,
        @Part("_method") method: RequestBody

    ): Response<UpdateAdsPropertyResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/ads_properties/{id}")
    suspend fun deleteAdsProperty(
        @Path("id") id : String
    ): Response<DeleteAdsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_property_details/{id}")
    suspend fun getPropertyDetails(
        @Path("id") id : String
    ): Response<PropertiesDetailsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/ads_properties/{id}")
    suspend fun getAdsDetail(
        @Path("id") id : String
    ): Response<AdsDetailResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/property_types")
    suspend fun getPropertyTypes(
    ): Response<PropertyTypeResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/amenities")
    suspend fun getAllAmenities(
    ): Response<AmenititesResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_property_multilisting")
    suspend fun getMultiListingProperties(): Response<MultiListingPropertyResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_tenant_property_details/{id}")
    suspend fun getTenantPropertyDetails(
        @Path("id") id : String
    ): Response<AdsDetailResponse>

    // tenants

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_my_tenants")
    suspend fun getMyTenants(
        @Query("sort") sort: String,
        @Query("keyword") keyword: String,
        @Query("search") search: String
    ): Response<MyTenantsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/tenants")
    suspend fun getAllTenants(): Response<AllTenantsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/tenants/{id}")
    suspend fun getTenantsById(
        @Path("id") id : String
    ): Response<TenantsByIdResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/tenants/{id}")
    suspend fun deleteTenantsById(
        @Path("id") id : String
    ): Response<DeleteTenantsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/lease_reminder")
    suspend fun leaseReminder(
        @Body request: LeaseReminderRequest
    ): Response<LeaseReminderResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/assign_property_to_tenant")
    suspend fun assignPropertyToTenants(
        @Body request: AssignPropertyRequest
    ): Response<AssignPropertyToTenantsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/unassigned_property")
    suspend fun unAssignPropertyToTenants(
        @Body request: UnassignPropertyRequest
    ): Response<UnassignPropertyResponse>

    // notes

    @Headers("Accept:application/json")
    @POST("/api/v1/notes")
    suspend fun addNoteToTenants(
        @Body request: AddNoteToTenantsRequest
    ): Response<AddNoteToTenantResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/notes/{id}")
    suspend fun deleteNote(
        @Path("id") id : String
    ): Response<DelNotesByIdResponse>

    //  billing

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_paid_bills")
    suspend fun getPaidBills(): Response<PaidBillsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_unpaid_bills")
    suspend fun getUnPaidBills(): Response<UnPaidBillsResponse>

    @Multipart
    @POST("/api/v1/bills")
    suspend fun sendInvoice(
        @Part("amount") amount: RequestBody,
        @Part("date") date: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("units") units: RequestBody,
        @Part("property_id") property_id: RequestBody,
        @Part("bill_type_id") bill_type_id: RequestBody,
        @Part("user_id") user_id: RequestBody,
    ): Response<SendInvoiceResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/bills/{id}")
    suspend fun deleteInvoice(
        @Path("id") id : String
    ): Response<DeleteInvoiceResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_properties_with_bill_types")
    suspend fun getPropertiesWithBillTypes(): Response<PropertiesWithBillTypeResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_paid_bills_by_tenant_id/{id}")
    suspend fun getPaidBillByTenantId(
        @Path("id") id : String
    ): Response<PaidBillByTenantIdResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_unpaid_bills_by_tenant_id/{id}")
    suspend fun getUnpaidBillByTenantId(
        @Path("id") id : String
    ): Response<UnPaidBillByTenantIdResponse>

    // user setting api

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_tenant_profile")
    suspend fun getUserProfile(): Response<UserProfileResponse>

    @Multipart
    @POST("/api/v1/update_user")
    suspend fun updateUserProfile(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part image:MultipartBody.Part,
        @Part("address") address: RequestBody,
        @Part("business_name") business_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
    ): Response<UpdateUserProfileResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/change_password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    // notification apis

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/notifications")
    suspend fun getAllNotifications(): Response<GetNotificationsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/read_notification/{id}")
    suspend fun readNotification(
        @Path("id") id : String
    ): Response<ReadNotificationResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_notification_count")
    suspend fun getNotificationCount(): Response<NotificationCountResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/push_notification_toggle")
    suspend fun pushNotificationToggle(): Response<PushNotificationToggleResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/email_notification_toggle")
    suspend fun emailNotificationToggle(): Response<EmailNotificationToggleResponse>


    // ---------------------------------------Request And Reports -------------------------------

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/maintenance_requests")
    suspend fun getMaintenanceWithFilter(
        @Query("keyword") keyword: String,
        @Query("date") date: String,
        @Query("status") status: String
    ): Response<MaintenanceWithFilterResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/maintenance_requests/{id}")
    suspend fun getMaintenanceWithFilterDetail(
        @Path("id") id: String,
        @Query("keyword") keyword: String,
        @Query("date") date: String,
        @Query("status") status: String
    ): Response<MaintenanceWithFilterDetailResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/incident_requests")
    suspend fun getIncidentWithFilter(
        @Query("keyword") keyword: String,
        @Query("date") date: String,
        @Query("status") status: String,
        @Query("page") page: String
    ): Response<IncidentWithFilterResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/incident_requests/{id}")
    suspend fun getIncidentWithFilterDetail(
        @Path("id") id: String,
        @Query("keyword") keyword: String,
        @Query("date") date: String,
        @Query("status") status: String
    ): Response<IncidentWithFilterDetailResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/event_types")
    suspend fun getAllEventTypes(): Response<AllEventTypesResponse>

    @Multipart
    @POST("/api/v1/events")
    suspend fun addNewEvent(
        @Part("title") title: RequestBody,
        @Part("date") date: RequestBody,
        @Part("description") description: RequestBody,
        @Part("event_type_id") event_type_id: RequestBody,
        @Part("property_id") property_id: RequestBody,
        @Part("time") time: RequestBody,
        @Part("address") address: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part images:List<MultipartBody.Part>

    ): Response<AddNewEventResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/events")
    suspend fun getEventsWithFilter(
        @Query("keyword") keyword: String,
        @Query("date") date: String,
        @Query("status") status: String
    ): Response<AllEventListResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/events/{id}")
    suspend fun getEventsDetail(
        @Path("id") id: String,
        @Query("keyword") keyword: String,
        @Query("date") date: String,
        @Query("status") status: String
    ): Response<EventsDetailResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/events/{id}")
    suspend fun deleteEvent(
        @Path("id") id: Int
    ): Response<DeleteEventResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/post_request_comment")
    suspend fun postRequestComment(
        @Body request: PostCommentRequest
    ): Response<PostCommentResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/change_request_status/{id}")
    suspend fun changeRequestStatus(
        @Path("id") id: String,
        @Query("keyword") keyword: String,
        @Query("date") date: String,
        @Query("status") status: String
    ): Response<ChangeRequestStatusResponse>

    // ---------------------------------- Tenant Side ------------------------------


    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_maintenance_request_list")
    suspend fun getMaintenanceRequest(
        @Query("request_type_id") requestTypeId: String,
        @Query("date") date: String
    ): Response<MaintenanceRequestResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/bill_types")
    suspend fun getBillsType(): Response<GetBillTypesResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_list_of_bills")
    suspend fun getListOfBills(
        @Query("status") status: String,
        @Query("bill_type_id") billTypeId: String
    ): Response<ListOfBillsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_incident_reports")
    suspend fun getIncidentReports(
        @Query("date") date: String
    ): Response<IncidentReportResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/view_bill_details/{id}")
    suspend fun viewBillDetails(
        @Path("id") id : Int
    ): Response<ViewBillDetailsResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_payment_history/{id}")
    suspend fun getPaymentHistory(
        @Path("id") id : String
    ): Response<PaymentHistoryResponse>


    @Multipart
    @POST("/api/v1/update_user")
    suspend fun updateTenantProfile(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part image:MultipartBody.Part,
        @Part("address") address: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
    ): Response<UpdateUserProfileResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/vehicles")
    suspend fun getVehicle(): Response<GetVehicleResponse>

    @Multipart
    @POST("/api/v1/vehicles")
    suspend fun addVehicle(
        @Part("vehicle_type") vehicleType: RequestBody,
        @Part("model") model: RequestBody,
        @Part("doors") doors: RequestBody,
        @Part("color") color: RequestBody,
        @Part("registration_date") registrationDate: RequestBody,
        @Part("licence_plate") licencePlate: RequestBody,
        @Part images:List<MultipartBody.Part>
    ): Response<AddVehicleResponse>

    @Multipart
    @POST("/api/v1/vehicles/{id}")
    suspend fun updateVehicle(
        @Path("id") id : Int,
        @Part("vehicle_type") vehicleType: RequestBody,
        @Part("model") model: RequestBody,
        @Part("doors") doors: RequestBody,
        @Part("color") color: RequestBody,
        @Part("registration_date") registrationDate: RequestBody,
        @Part("licence_plate") licencePlate: RequestBody,
        @Part images:List<MultipartBody.Part>,
        @Part("_method") method: RequestBody,
    ): Response<UpdateVehicleResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @DELETE("/api/v1/vehicles/{id}")
    suspend fun deleteVehicle(
        @Path("id") id : String
    ): Response<DeleteVehicleResponse>


    @Multipart
    @POST("api/v1/request_report")
    suspend fun addRequestReport(
        @Part("request_type_id") requestTypeId:RequestBody,
        @Part("property_id") propertyId:RequestBody,
        @Part("title") title:RequestBody,
        @Part("description") description:RequestBody,
        @Part images:List<MultipartBody.Part>,
    ):Response<SendReportResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_all_assign_properties")
    suspend fun getAllAssignProperties(
    ): Response<AllAssignPropertiesResponse>

    //  ---------------------------------------------Terms Conditions -------------------------
    // 2 -> Terms
    // 3 -> privacy policy
    // 4 -> help support
    // 5 -> Tips

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/pages/{id}")
    suspend fun getTerms(
        @Path("id") id : Int
    ): Response<TermsConditionResponse>

    // ------------------------------------ Tenant Payment -------------------------------

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/schedules")
    suspend fun getSchedulesPaymentList(
    ): Response<SchedulesPaymentListResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/schedules")
    suspend fun addSchedulesPayment(
        @Body request: AddPaymentScheduleRequest
    ): Response<AddPaymentScheduleResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/schedules/{id}")
    suspend fun updateSchedulesPayment(
        @Path("id") id : Int,
        @Body request: UpdatePaymentScheduleRequest
    ): Response<UpdatePaymentScheduleResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/schedules/{id}")
    suspend fun deleteSchedulesPayment(
        @Path("id") id : Int
    ): Response<DeleteScheduleResponse>

    // ---------------------------------- payment settings ---------------------------------------


    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_all_plans")
    suspend fun getPlans(
    ): Response<SubscriptionPlansResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/create_customer/fake-valid-nonce")
    suspend fun createCustomer(
        @Body request: CreateCustomerRequest
    ): Response<CreateCustomerResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/update_customer_card/{nounce_id}")
    suspend fun updateCustomerCard(
        @Path("nounce_id") nounce_id : String,
        @Body request: UpdateCustomerRequest
    ): Response<UpdateCustomerResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_card_detail")
    suspend fun getCardDetail(
    ): Response<CardDetailResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_client_token")
    suspend fun getClientToken(
    ): Response<ClientTokenResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/pay_invoice")
    suspend fun payInvoice(
        @Body request: PayBillRequest
    ): Response<PayBillResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/make_subscriptions")
    suspend fun makeSubscriptions(
        @Body request: MakeSubscriptionsRequest
    ): Response<LoginResponse>


}