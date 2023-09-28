package com.jeuxdevelopers.estatepie.repos.tenants

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
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.network.responses.others.TermsConditionResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.AddPaymentScheduleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.DeleteScheduleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.SchedulesPaymentListResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.UpdatePaymentScheduleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.*
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.AddVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.DeleteVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.GetVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.UpdateVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.AllAssignPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.SendReportResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.toggle.ToggleResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow

interface TenantsPropertiesRepo {

    fun getPropertiesDetails(request: PropertyDetailRequest): Flow<NetworkResult<AdsDetailResponse>>

    fun getPropertyTypes(): Flow<NetworkResult<PropertyTypeResponse>>

    fun getAssignProperties(): Flow<NetworkResult<AllAssignPropertiesResponse>>

    fun toggleEmailNotification(token: String):Flow<NetworkResult<ToggleResponse>>

    fun togglePushNotification(token: String):Flow<NetworkResult<ToggleResponse>>

    //  --------------------------------- billing ------------------------------

    fun getPaidBills() : Flow<NetworkResult<PaidBillsResponse>>

    fun getUnPaidBills() : Flow<NetworkResult<UnPaidBillsResponse>>

    // ----------------------------------------- profile ------------------------------

    fun getVehicleList() : Flow<NetworkResult<GetVehicleResponse>>

    fun addVehicle(request: AddVehicleRequest) : Flow<NetworkResult<AddVehicleResponse>>

    fun updateVehicle(request: UpdateVehicleRequest) : Flow<NetworkResult<UpdateVehicleResponse>>

    fun deleteVehicle(request: DeleteVehicleRequest) : Flow<NetworkResult<DeleteVehicleResponse>>

    // ---------------------------------- Request Report - ---------------------------

    fun addRequestReport(request: SendReportRequest):Flow<NetworkResult<SendReportResponse>>

    // ---------------------------------- Dashboard ----------------------------------

    fun getMaintenanceRequests(request: MaintenanceRequestRequest):Flow<NetworkResult<MaintenanceRequestResponse>>

    fun getIncidentReports(request: IncidentReportRequest):Flow<NetworkResult<IncidentReportResponse>>

    fun getUpcomingBills(request: ListOfBillsRequest):Flow<NetworkResult<ListOfBillsResponse>>

    fun getBillTypes():Flow<NetworkResult<GetBillTypesResponse>>

    fun viewBillDetail(id: Int):Flow<NetworkResult<ViewBillDetailsResponse>>

    // -----------------------------------Terms -------------------------------------

    fun getTerms(id: Int):Flow<NetworkResult<TermsConditionResponse>>

    // ------------------------------- Tenant Payment-------------------------------

    fun getPaymentScheduleList():Flow<NetworkResult<SchedulesPaymentListResponse>>

    fun addPaymentSchedule(request: AddPaymentScheduleRequest):Flow<NetworkResult<AddPaymentScheduleResponse>>

    fun updatePaymentSchedule(id: Int,request: UpdatePaymentScheduleRequest):Flow<NetworkResult<UpdatePaymentScheduleResponse>>

    fun deletePaymentSchedule(id: Int):Flow<NetworkResult<DeleteScheduleResponse>>


}