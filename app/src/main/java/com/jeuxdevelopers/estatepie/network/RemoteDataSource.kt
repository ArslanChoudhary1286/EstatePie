package com.jeuxdevelopers.estatepie.network

import com.jeuxdevelopers.estatepie.network.requests.auth.DeleteAccountRequest
import com.jeuxdevelopers.estatepie.network.requests.auth.ForgotPasswordRequest
import com.jeuxdevelopers.estatepie.network.requests.auth.ResendCodeRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.AdsDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ExploreRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.MarkToFavoriteRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ViewAllRecomRequest
import com.jeuxdevelopers.estatepie.network.requests.management.auth.ManagementSignupRequest
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
import com.jeuxdevelopers.estatepie.network.requests.plans.CreateCustomerRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.MakeSubscriptionsRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.PayBillRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.UpdateCustomerRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.CheckSocialUserRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.SocialLoginRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.TenantLoginRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.TenantSignupRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.billing.AddPaymentScheduleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.billing.UpdatePaymentScheduleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.IncidentReportRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.ListOfBillsRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.MaintenanceRequestRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.profile.AddVehicleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.profile.DeleteVehicleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.profile.UpdateVehicleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.properties.PropertyDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.request.SendReportRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.create
import java.io.File
import javax.inject.Inject


class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val apiAuthService: ApiAuthService
) {

    suspend fun signUpTenantUser(request: TenantSignupRequest) =
        apiService.tenantSignUp(
            email = request.email,
            password = request.password,
            name = request.name,
            device_type = request.device_type,
            password_confirmation = request.password_confirmation,
            device_token = request.device_token,
            first_name = request.first_name,
            last_name = request.last_name,
            phone = request.phone
        )

    suspend fun signUpManagementUser(request: ManagementSignupRequest) =
        apiService.managementSignUp(
            email = request.email,
            password = request.password,
            name = request.name,
            device_type = request.device_type,
            password_confirmation = request.password_confirmation,
            device_token = request.device_token,
            first_name = request.first_name,
            last_name = request.last_name,
            business_name = request.business_name,
            address = request.address

        )

    suspend fun loginUser(request: TenantLoginRequest) =
        apiService.loginUser(
            email = request.email,
            password = request.password,
            device_type = request.device_type)

    suspend fun socialLogin(request: SocialLoginRequest) =
        apiService.socialLogin(
            platform = request.platform,
            client_id = request.client_id,
            token = request.token,
            username = request.username,
            email = request.email,
            device_token = request.device_token,
            device_type = request.device_type,
            role_id = request.role_id,
            business_name = request.business_name)

    suspend fun checkSocialUser(request: CheckSocialUserRequest) =
        apiService.checkSocialUser(
            platform = request.platform,
            client_id = request.client_id
        )

    suspend fun resendCode(request: ResendCodeRequest) =
        apiService.resendCode(
            verification_email = request.verification_email)

    suspend fun forgotPassword(request: ForgotPasswordRequest) =
        apiService.forgotPassword(
            email = request.email
        )

    suspend fun deleteAccount(request: DeleteAccountRequest) =
        apiAuthService.deleteAccount(
            device_type = request.device_type,
            device_token = request.device_token
        )

    suspend fun getExploreList(request: ExploreRequest) =
        apiService.getExploreList(
            lat = request.lat,
            lng = request.lng
        )

    suspend fun getViewAllRecommendedList(request: ViewAllRecomRequest) =
        apiService.getViewAllRecommendedList(
            price_range_start = request.price_range_start,
            price_range_end = request.price_range_end
        )

    suspend fun leasingTerms() =
        apiService.leasingTerms()


    // ===============================================Access with Token===================================

    suspend fun markToFavorite(request: MarkToFavoriteRequest) =
        apiAuthService.markToFavorite(
            id = request.id
        )

    suspend fun getAllFavorites() =
        apiAuthService.getAllFavorites()

    suspend fun getAllReports(request: AllReportsRequest) =
        apiAuthService.getAllReports(
            page = request.page,
            reportType = request.report_type,
            date = request.date

        )

    suspend fun getRequestByPropertyId(request: RequestsByPropertyIdRequest) =
        apiAuthService.getRequestsByPropertyId(
            id = request.id,
            page = request.page
        )

    suspend fun getRequestsDetailById(request: Int) =
        apiAuthService.getRequestsDetailById(
            id = request
        )

    suspend fun getRentFees(request: RentFeesRequest) =
        apiAuthService.getRentFees(
            keyword = request.keyword,
            months = request.months
        )

    suspend fun getUtilityBills() =
        apiAuthService.getUtilityBills()

    suspend fun getPropertyBills(request: PropertyBillsRequest) =
        apiAuthService.getPropertyBills(
            id = request.id,
            status = request.status
        )

    suspend fun getResidentialProperties(request: GetResidentialRequest) =
        apiAuthService.getResidentialProperties(
            sort = request.sort,
            keyword = request.keyword,
            search = request.search,
            page = request.page
        )

    suspend fun addResidentialProperty(request: AddResidentialRequest) =
        apiAuthService.addResidentialProperty(
            name = createRequest(request.name),
            category_id = createRequest(request.category_id),
            apt_number = createRequest(request.apt_number),
            number_range_from = createRequest(request.number_range_from),
            number_range_to = createRequest(request.number_range_to),
            range_alpha_from = createRequest(request.range_alpha_from),
            range_alpha_to = createRequest(request.range_alpha_to),
            address = createRequest(request.address),
            latitude = createRequest(request.latitude),
            longitude = createRequest(request.longitude),
            notes = createRequest(request.notes),
            status = createRequest(request.status),
            images = uploadMultiImagesFile(request.images)
        )

    suspend fun updateResidentialProperty(request: UpdateResidentialRequest) =
        apiAuthService.updateResidentialProperty(
            id = request.id,
            name = createRequest(request.name),
            category_id = createRequest(request.category_id),
            unit = createRequest(request.unit),
//            number_range_from = createRequest(request.number_range_from),
//            number_range_to = createRequest(request.number_range_to),
//            range_alpha_from = createRequest(request.range_alpha_from),
//            range_alpha_to = createRequest(request.range_alpha_to),
            address = createRequest(request.address),
            latitude = createRequest(request.latitude),
            longitude = createRequest(request.longitude),
            notes = createRequest(request.notes),
            method = createRequest(request._method),
//            status = createRequest(request.status),
            images = uploadMultiImagesFile(request.images)
        )


    suspend fun deleteResidentialProperty(request: DeleteResidentialRequest) =
        apiAuthService.deleteResidentialProperty(
            id = request.id
        )

    suspend fun getCommercialProperties(request: GetCommercialRequest) =
        apiAuthService.getCommercialProperties(
            sort = request.sort,
            keyword = request.keyword,
            search = request.search,
            page = request.page
        )

    suspend fun addCommercialProperty(request: AddCommercialRequest) =
        apiAuthService.addCommercialProperty(
            name = createRequest(request.name),
            category_id = createRequest(request.category_id),
            apt_number = createRequest(request.apt_number),
            address = createRequest(request.address),
            latitude = createRequest(request.latitude),
            longitude = createRequest(request.longitude),
            notes = createRequest(request.notes),
            status = createRequest(request.status),
            images = uploadMultiImagesFile(request.images)
        )

    suspend fun updateCommercialProperty(request: UpdateCommerecialRequest) =
        apiAuthService.updateCommercialProperty(
            id = request.id,
            name = createRequest(request.name),
            category_id = createRequest(request.category_id),
            unit = createRequest(request.unit),
            apt = createRequest(request.apt_number),
//            number_range_from = createRequest(request.number_range_from),
//            number_range_to = createRequest(request.number_range_to),
//            range_alpha_from = createRequest(request.range_alpha_from),
//            range_alpha_to = createRequest(request.range_alpha_to),
            address = createRequest(request.address),
            latitude = createRequest(request.latitude),
            longitude = createRequest(request.longitude),
            notes = createRequest(request.notes),
            method = createRequest(request._method),
//            status = createRequest(request.status),
            images = uploadMultiImagesFile(request.images)
        )

    suspend fun deleteCommercialProperty(request: DeleteCommercialRequest) =
        apiAuthService.deleteCommercialProperty(
            id = request.id
        )

    suspend fun getAdsProperties(request: GetAdsRequest) =
        apiAuthService.getAdsProperties(
            sort = request.sort,
            keyword = request.keyword,
            search = request.search,
            page = request.page
        )

    suspend fun addAdsProperty(request: AddAdsRequest) =
        apiAuthService.addAdsProperty(
            name = createRequest(request.name),
            category = createRequest(request.category),
            purpose = createRequest(request.purpose),
            unit = createRequest(request.unit),
            size = createRequest(request.size),
            broker = createRequest(request.broker),
            price = createRequest(request.price),
            address = createRequest(request.address),
            description = createRequest(request.description),
            lease = createRequest(request.lease),
            beds = createRequest(request.beds),
            fees = createRequest(request.fees),
            latitude = createRequest(request.latitude),
            longitude = createRequest(request.longitude),
            multiunits = createRequest(request.multiunits),
            available = createRequest(request.available),
            images = uploadMultiImagesFile(request.images),
            amenities = createRequest(request.amenities),
            property_type = createRequest(request.property_type),
            lease_terms = createRequest(request.lease_terms),
            smoking = createRequest(request.smoking),
            pets = createRequest(request.pets),
            bathroom = createRequest(request.bathroom)

        )

    suspend fun updateAdsProperty(request: UpdateAdsRequest) =
        apiAuthService.updateAdsProperty(
            id = request.id,
            name = createRequest(request.name),
            category = createRequest(request.category),
            purpose = createRequest(request.purpose),
            unit = createRequest(request.unit),
            size = createRequest(request.size),
            broker = createRequest(request.broker),
            price = createRequest(request.price),
            address = createRequest(request.address),
            description = createRequest(request.description),
            lease = createRequest(request.lease),
            beds = createRequest(request.beds),
            fees = createRequest(request.fees),
            latitude = createRequest(request.latitude),
            longitude = createRequest(request.longitude),
            multiunits = createRequest(request.multiunits),
            available = createRequest(request.available),
            images = uploadMultiImagesFile(request.images),
            amenities = createRequest(request.amenities),
            property_type = createRequest(request.property_type),
            lease_terms = createRequest(request.lease_terms),
            method = createRequest(request._method)

        )

    suspend fun deleteAdsProperty(request: DeleteAdsRequest) =
        apiAuthService.deleteAdsProperty(
            id = request.id
        )


    suspend fun getAdsDetail(request: AdsDetailRequest) =
        apiAuthService.getAdsDetail(
            id = request.id
        )

    suspend fun getPropertyDetails(request: PropertyDetailRequest) =
        apiAuthService.getPropertyDetails(
            id = request.id
        )

    suspend fun getPropertyTypes() =
        apiAuthService.getPropertyTypes()

    suspend fun getAllAssignProperties() =
        apiAuthService.getAllAssignProperties()

    suspend fun getAllAmenities() =
        apiAuthService.getAllAmenities()

    suspend fun getMultiListingProperties() =
        apiAuthService.getMultiListingProperties()


    suspend fun getTenantPropertyDetails(request: PropertyDetailRequest) =
        apiAuthService.getTenantPropertyDetails(
            id = request.id
        )

    suspend fun getMyTenants(request: MyTenantsRequest) =
        apiAuthService.getMyTenants(
          sort =  request.sort,
          keyword = request.keyword,
          search = request.search
        )

    suspend fun getAllTenants() =
        apiAuthService.getAllTenants()

    suspend fun getTenantsById(request: GetTenantsByIdRequest) =
        apiAuthService.getTenantsById(
            id  = request.id
        )

    suspend fun deleteTenantsById(request: DeleteTenantsByIdRequest) =
        apiAuthService.deleteTenantsById(
            id  = request.id
        )

    suspend fun leaseReminder(request: LeaseReminderRequest) =
        apiAuthService.leaseReminder(
            request
        )

    suspend fun assignPropertyToTenants(request: AssignPropertyRequest) =
        apiAuthService.assignPropertyToTenants(
            request
        )

    suspend fun unAssignPropertyToTenants(request: UnassignPropertyRequest) =
        apiAuthService.unAssignPropertyToTenants(
            request
        )

    suspend fun addNoteToTenants(request: AddNoteToTenantsRequest) =
        apiAuthService.addNoteToTenants(
            request
        )

    suspend fun deleteNote(request: DelNoteToTenantsRequest) =
        apiAuthService.deleteNote(
            id = request.id
        )

    suspend fun getPaidBills() =
        apiAuthService.getPaidBills( )

    suspend fun getUnPaidBills() =
        apiAuthService.getUnPaidBills( )

    suspend fun sendInvoice(request: SendInvoiceRequest) =
        apiAuthService.sendInvoice(
            amount = createRequest(request.amount),
            date = createRequest(request.date),
            image = request.image!!.toMultipartImageFile("image"),
            units = createRequest(request.units),
            property_id = createRequest(request.property_id),
            bill_type_id = createRequest(request.bill_type_id),
            user_id = createRequest(request.user_id)
        )

    suspend fun deleteInvoice(request: DeleteInvoiceRequest) =
        apiAuthService.deleteInvoice(
            id = request.id
        )

    suspend fun getPropertiesWithBillTypes() =
        apiAuthService.getPropertiesWithBillTypes( )

    suspend fun getPaidBillByTenantId(request: PaidBillByTenantIdRequest) =
        apiAuthService.getPaidBillByTenantId(
            id = request.id
        )

    suspend fun getUnpaidBillByTenantId(request: UnPaidBillByTenantIdRequest) =
        apiAuthService.getUnpaidBillByTenantId(
            id = request.id
        )

    suspend fun getMaintenanceWithFilter(request: MaintenanceWithFilterRequest) =
        apiAuthService.getMaintenanceWithFilter(
            keyword = request.keyword,
            date = request.date,
            status = request.status
        )

    suspend fun getMaintenanceWithFilterDetail(request: MaintenanceWithFilterDetailRequest) =
        apiAuthService.getMaintenanceWithFilterDetail(
            id = request.id,
            keyword = request.keyword,
            date = request.date,
            status = request.status
        )

    suspend fun changeRequestStatus(request: ChangeRequestStatusRequest) =
        apiAuthService.changeRequestStatus(
            id = request.id,
            keyword = request.keyword,
            date = request.date,
            status = request.status
        )

    suspend fun postComment(request: PostCommentRequest) =
        apiAuthService.postRequestComment(
            request
        )

    suspend fun getIncidentWithFilter(request: IncidentWithFilterRequest) =
        apiAuthService.getIncidentWithFilter(
            keyword = request.keyword,
            date = request.date,
            status = request.status,
            page = request.page
        )

    suspend fun getIncidentWithFilterDetail(request: IncidentWithFilterDetailRequest) =
        apiAuthService.getIncidentWithFilterDetail(
            id = request.id,
            keyword = request.keyword,
            date = request.date,
            status = request.status
        )

    suspend fun getEventsList(request: AllEventListRequest) =
        apiAuthService.getEventsWithFilter(
            keyword = request.keyword,
            date = request.date,
            status = request.status
        )

    suspend fun getEventsDetail(request: EventsDetailRequest) =
        apiAuthService.getEventsDetail(
            id = request.id,
            keyword = request.keyword,
            date = request.date,
            status = request.status
        )

    suspend fun deleteEvent(id: Int) =
        apiAuthService.deleteEvent(
            id = id
        )

    suspend fun addNewEvent(request: AddNewEventRequest) =
        apiAuthService.addNewEvent(
            title =  createRequest(request.title),
            date =  createRequest(request.date),
            description =  createRequest(request.description),
            event_type_id =  createRequest(request.event_type_id),
            property_id =  createRequest(request.property_id),
            time =  createRequest(request.time),
            address = createRequest(request.address),
            latitude =  createRequest(request.latitude),
            longitude = createRequest(request.longitude),
            images = uploadMultiImagesFile(request.images)
        )

    suspend fun getEventsTypes() =
        apiAuthService.getAllEventTypes()

    suspend fun getSampleCSV() =
        apiAuthService.getSampleCSV()

    suspend fun uploadCSV(request: UploadCSVRequest) =
        apiAuthService.uploadCSV(
            id = createRequest(request.id),
            file = request.file!!.toMultipartDocsFile("file"),
        )
//---------------------------------  User Settings -----------------------------------------------

    suspend fun getUserProfile() =
        apiAuthService.getUserProfile()

    suspend fun updateUserProfile(request: UpdateUserProfileRequest) =
        apiAuthService.updateUserProfile(
            first_name = createRequest(request.first_name),
            last_name = createRequest(request.last_name),
            image = request.image!!.toMultipartImageFile("image"),
            address = createRequest(request.address),
            business_name = createRequest(request.business_name),
            email = createRequest(request.email),
            phone = createRequest(request.phone),
            latitude = createRequest(request.latitude),
            longitude = createRequest(request.longitude)
        )

    suspend fun changePassword(request: ChangePasswordRequest) =
        apiAuthService.changePassword(request)

    // notifications...

    suspend fun getAllNotifications() =
        apiAuthService.getAllNotifications()

    suspend fun readNotification(request: ReadNotificationRequest) =
        apiAuthService.readNotification(
            id = request.id
        )

    suspend fun getNotificationCount() =
        apiAuthService.getNotificationCount()

    suspend fun pushNotificationToggle() =
        apiAuthService.pushNotificationToggle()

    suspend fun emailNotificationToggle() =
        apiAuthService.emailNotificationToggle()

    suspend fun getPropertyTypes(token: String) =
        apiAuthService.getPropertyTypes()

    suspend fun getVehicleList() =
        apiAuthService.getVehicle()

    suspend fun addVehicle(request: AddVehicleRequest) =
        apiAuthService.addVehicle(
            vehicleType = createRequest(request.vehicle_type),
            model = createRequest(request.model),
            doors = createRequest(request.doors),
            color = createRequest(request.color),
            registrationDate = createRequest(request.registration_date),
            licencePlate = createRequest(request.licence_plate),
            images = uploadMultiImagesFile(request.images)
        )

    suspend fun updateVehicle(request: UpdateVehicleRequest) =
        apiAuthService.updateVehicle(
            id = request.id,
            vehicleType = createRequest(request.vehicle_type),
            model = createRequest(request.model),
            doors = createRequest(request.doors),
            color = createRequest(request.color),
            registrationDate = createRequest(request.registration_date),
            licencePlate = createRequest(request.licence_plate),
            images = uploadMultiImagesFile(request.images),
            method = createRequest(request._method)
        )


    suspend fun deleteVehicle(request: DeleteVehicleRequest) =
        apiAuthService.deleteVehicle(
            id = request.id
        )


    suspend fun addRequestReport(request: SendReportRequest) =
        apiAuthService.addRequestReport(
            requestTypeId = createRequest(request.request_type_id),
            propertyId = createRequest(request.property_id),
            title = createRequest(request.title),
            description = createRequest(request.description),
            images = uploadMultiImagesFile(request.images)
        )

    suspend fun getMaintenanceRequests(request: MaintenanceRequestRequest) =
        apiAuthService.getMaintenanceRequest(
            requestTypeId = request.request_type_id,
            date = request.date
        )

    suspend fun getIncidentReports(request: IncidentReportRequest) =
        apiAuthService.getIncidentReports(
            date = request.date
        )

    suspend fun getUpcomingBills(request: ListOfBillsRequest) =
        apiAuthService.getListOfBills(
            status = request.status,
            billTypeId = request.bill_type_id
        )

    suspend fun getBillTypes() =
        apiAuthService.getBillsType()

    suspend fun viewBillDetails(id: Int) =
        apiAuthService.viewBillDetails(
            id = id
        )

    suspend fun getTerms(id : Int) =
        apiAuthService.getTerms(
            id = id
        )

    suspend fun updatePassword(userId: Int, oldPassword: String, newPassword: String) =
        apiService.updateUserPassword(userId, oldPassword, newPassword)


    suspend fun togglePushNotification(token: String) =
        apiService.togglePushNotification("bearer $token", "application/json")

    suspend fun toggleEmailNotification(token: String) =
        apiService.toggleEmailNotification("bearer $token", "application/json")

    // ----------------------------------Tenant Payment Schedule ----------------------------


    suspend fun getPaymentScheduleList() =
        apiAuthService.getSchedulesPaymentList()

    suspend fun addPaymentSchedule(request: AddPaymentScheduleRequest) =
        apiAuthService.addSchedulesPayment(
            request
        )

    suspend fun updatePaymentSchedule(id: Int,request: UpdatePaymentScheduleRequest) =
        apiAuthService.updateSchedulesPayment(
            id = id,
            request
        )

    suspend fun deletePaymentSchedule(id: Int) =
        apiAuthService.deleteSchedulesPayment(
            id = id
        )



    // ----------------------------- Plans ---------------------------------------------

    suspend fun getSubscriptionPlans() =
        apiAuthService.getPlans()

    suspend fun makeSubscriptions(request: MakeSubscriptionsRequest) =
        apiAuthService.makeSubscriptions(request)

    suspend fun getCardDetail() =
        apiAuthService.getCardDetail()

    suspend fun addCustomerCard(request: CreateCustomerRequest) =
        apiAuthService.createCustomer(request)

    suspend fun updateCustomerCard(id: String, request: UpdateCustomerRequest) =
        apiAuthService.updateCustomerCard(id, request)

    suspend fun payBill(request: PayBillRequest) =
        apiAuthService.payInvoice(request)


    // ---------------------------------- Funcs --------------------------------


    private fun File.toMultipartImageFile(name: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name,
            this.name,
            this.asRequestBody("image/*".toMediaTypeOrNull())
        )

    }

    private fun File.toMultipartDocsFile(name: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name,
            this.name,
            this.asRequestBody("text/plain".toMediaTypeOrNull())
        )

    }

//    var Title: RequestBody = create(MediaType.parse("text/plain"), namepdf)
//    var PDFreq: RequestBody = create(MediaType.parse("application/pdf"), file.getName())

    private fun uploadMultiImagesFile(images: List<File>): List<MultipartBody.Part> {

        val list: MutableList<MultipartBody.Part> = mutableListOf()

        images.forEachIndexed { index, image ->
            list.add(index, image.toMultipartImagesFile("images[${index}]", image.name))
        }

        return list
    }

    private fun File.toMultipartImagesFile(name: String, imageName: String): MultipartBody.Part {

        return MultipartBody.Part.createFormData(
            name,
            this.name,
            this.asRequestBody("image/*".toMediaTypeOrNull())
        )

    }

    fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }




//    val requestBody = MultipartBody.Builder()
//        .setType(MultipartBody.FORM)
//
//    galleryListArray.forEachIndexed { index, image ->
//        requestBody.addFormDataPart("attachment[${index}]", image.toString())
//    }

//    requestBody.build()

    private fun createRequest(content: String): RequestBody {

        return create(MultipartBody.FORM, content)

    }

}