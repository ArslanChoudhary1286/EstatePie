package com.jeuxdevelopers.estatepie.repos.tenants

import com.jeuxdevelopers.estatepie.network.BaseApiResponse
import com.jeuxdevelopers.estatepie.network.RemoteDataSource
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
import com.jeuxdevelopers.estatepie.network.responses.tenants.others.PropertyTypesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.AddVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.DeleteVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.GetVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.UpdateVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.AllAssignPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.SendReportResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.toggle.ToggleResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class TenantsPropertiesRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : TenantsPropertiesRepo, BaseApiResponse() {


    override fun getPropertiesDetails(request: PropertyDetailRequest): Flow<NetworkResult<AdsDetailResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getTenantPropertyDetails(request) })
        }.flowOn(Dispatchers.IO)

    override fun getPropertyTypes(): Flow<NetworkResult<PropertyTypeResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPropertyTypes() })
        }.flowOn(Dispatchers.IO)

    override fun getAssignProperties(): Flow<NetworkResult<AllAssignPropertiesResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getAllAssignProperties() })
        }.flowOn(Dispatchers.IO)

    override fun toggleEmailNotification(token: String): Flow<NetworkResult<ToggleResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.toggleEmailNotification(token) })
        }.flowOn(Dispatchers.IO)


    override fun togglePushNotification(token: String): Flow<NetworkResult<ToggleResponse>> = flow {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.togglePushNotification(token) })
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

    override fun getVehicleList(): Flow<NetworkResult<GetVehicleResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getVehicleList() })
        }.flowOn(Dispatchers.IO)

    override fun addVehicle(request: AddVehicleRequest): Flow<NetworkResult<AddVehicleResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addVehicle(request) })
        }.flowOn(Dispatchers.IO)

    override fun updateVehicle(request: UpdateVehicleRequest): Flow<NetworkResult<UpdateVehicleResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.updateVehicle(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteVehicle(request: DeleteVehicleRequest): Flow<NetworkResult<DeleteVehicleResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteVehicle(request) })
        }.flowOn(Dispatchers.IO)

    override fun addRequestReport(request: SendReportRequest): Flow<NetworkResult<SendReportResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addRequestReport(request) })
        }.flowOn(Dispatchers.IO)

    override fun getMaintenanceRequests(request: MaintenanceRequestRequest): Flow<NetworkResult<MaintenanceRequestResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getMaintenanceRequests(request) })
        }.flowOn(Dispatchers.IO)

    override fun getIncidentReports(request: IncidentReportRequest): Flow<NetworkResult<IncidentReportResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getIncidentReports(request) })
        }.flowOn(Dispatchers.IO)

    override fun getUpcomingBills(request: ListOfBillsRequest): Flow<NetworkResult<ListOfBillsResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getUpcomingBills(request) })
        }.flowOn(Dispatchers.IO)

    override fun getBillTypes(): Flow<NetworkResult<GetBillTypesResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getBillTypes() })
        }.flowOn(Dispatchers.IO)

    override fun viewBillDetail(id: Int): Flow<NetworkResult<ViewBillDetailsResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.viewBillDetails(id) })
        }.flowOn(Dispatchers.IO)

    override fun getTerms(id: Int): Flow<NetworkResult<TermsConditionResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getTerms(id) })
        }.flowOn(Dispatchers.IO)

    override fun getPaymentScheduleList(): Flow<NetworkResult<SchedulesPaymentListResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getPaymentScheduleList() })
        }.flowOn(Dispatchers.IO)

    override fun addPaymentSchedule(request: AddPaymentScheduleRequest): Flow<NetworkResult<AddPaymentScheduleResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addPaymentSchedule(request) })
        }.flowOn(Dispatchers.IO)

    override fun updatePaymentSchedule(id: Int,request: UpdatePaymentScheduleRequest): Flow<NetworkResult<UpdatePaymentScheduleResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.updatePaymentSchedule(id, request) })
        }.flowOn(Dispatchers.IO)

    override fun deletePaymentSchedule(id: Int): Flow<NetworkResult<DeleteScheduleResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deletePaymentSchedule(id) })
        }.flowOn(Dispatchers.IO)

}