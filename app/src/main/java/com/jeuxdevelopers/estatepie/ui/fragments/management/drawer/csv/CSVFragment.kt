package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.csv

import android.Manifest
import android.R.attr.data
import android.net.Uri
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jeuxdevelopers.estatepie.R
import com.jeuxdevelopers.estatepie.databinding.FragmentCSVBinding
import com.jeuxdevelopers.estatepie.network.responses.management.csv.GetSampleCSVResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.ui.dialogs.waiting.WaitingDialog
import com.jeuxdevelopers.estatepie.utils.ExtensionUtils.collectLatestLifecycleFlow
import com.jeuxdevelopers.estatepie.utils.FileUtils
import com.jeuxdevelopers.estatepie.utils.ToastUtility
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.*
import timber.log.Timber
import java.io.*
import java.util.regex.Pattern


@AndroidEntryPoint
class CSVFragment : Fragment() {

    private lateinit var binding: FragmentCSVBinding

    private val viewModel: CSVViewModel by viewModels()

    private var pickFile: ActivityResultLauncher<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCSVBinding.inflate(inflater, container, false)

        initCollectors()

        initResultLauncher()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getCsvFiles()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()

    }

    private fun initCollectors() {

        val waitingDialog = WaitingDialog(requireContext())
        collectLatestLifecycleFlow(viewModel.sampleUiState){
            when(it){

                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading ...")
                    Timber.d("user profile ")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    it.result!!.data.forEachIndexed{index, data ->

                        viewModel.sampleCsvList.add(data)

                    }

                    if (it.result.data.isNotEmpty())
                    viewModel.downloadUrl = viewModel.sampleCsvList[0].file
                    viewModel.id = viewModel.sampleCsvList[0].id.toString()
                    viewModel.fileName = viewModel.sampleCsvList[0].name.toString()

                    setCategoryAdapter( it.result.data)

                }
            }
        }

        collectLatestLifecycleFlow(viewModel.uploadCSVUiState){
            when(it){

                is NetworkResult.Error -> {
                    waitingDialog.dismiss()
                    Timber.d("Error -> ${it.message}, ${it.result.toString()}")
                    ToastUtility.errorToast(requireContext(), it.message)

                }
                is NetworkResult.Idle -> {
                    //nothing to do this is just idle state
                }
                is NetworkResult.Loading -> {
                    waitingDialog.show(status = "Loading ...")
                    Timber.d("user profile ")
                }
                is NetworkResult.Success -> {
                    waitingDialog.dismiss()
                    Timber.d("Success -> ${it.message}")
                    Timber.d("Success Data -> ${it.result?.data.toString()}")

                    ToastUtility.successToast(requireContext(), it.result?.message)
//                    findNavController().navigateUp()

                }
            }
        }

    }

    private fun setCategoryAdapter(properties: List<GetSampleCSVResponse.Data>) {

        val array = Array<String>(properties.size){""}

        properties.forEachIndexed { index, element ->
            array[index] = element.name
        }

        val propertyAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_string_adapter,
            R.id.adapterTxtView, array)

        binding.etProperty.setAdapter(propertyAdapter)

        binding.etProperty.setOnItemClickListener{parent, view, position, id ->

            viewModel.fileName = viewModel.sampleCsvList[position].name
            viewModel.downloadUrl = viewModel.sampleCsvList[position].file
            viewModel.id = viewModel.sampleCsvList[position].id.toString()

        }

    }

    private fun initClickListeners() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.downloadSample.setOnClickListener {

            ToastUtility.successToast(requireContext(), "downloading starts...")
            downloadFileInInternalStorage(viewModel.downloadUrl, "file.csv")

        }

        binding.browseFile.setOnClickListener {

            pickFile()

        }

    }

    private fun pickFile() {

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

//                    pickFile?.launch("image/*")
//                    pickFile?.launch("application/pdf")
                    pickFile?.launch("text/csv")

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    if (response.isPermanentlyDenied) {

                        ToastUtility.errorToast(
                            requireContext(), getString(R.string.enable_read_storage_permission)
                        )

                    } else {

                        ToastUtility.errorToast(
                            requireContext(), getString(R.string.read_storage_permission_required)
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest, permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }

            }).check()

    }

    private fun initResultLauncher() {

        pickFile = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->

            result?.let { uris ->

                val uri: Uri = uris
                val file = File(uri.toString())

                viewModel.uploadCSVRequest.file = File(file.absolutePath)
                viewModel.uploadCSVRequest.id = viewModel.id

                viewModel.uploadCsvFile()

            }

        }
    }

    private fun downloadFileInInternalStorage(link: String, fileName: String) {
        val mFileName = fileName.replace(" ", "_")
            .replace(Pattern.compile("[.][.]+").toRegex(), ".")
        val request = Request.Builder()
            .url(link).build()
        val client = OkHttpClient.Builder().build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {

                    val isDownload = writeResponseBodyToDisk(response.body!!)
                    Timber.d("Downloaded -----> " + isDownload)

                    Handler(Looper.getMainLooper()).post {

                        if (isDownload)
                            ToastUtility.successToast(requireContext(), "Download successfully.")
                        else
                            ToastUtility.errorToast(requireContext(), "Download Failed. ")
                    }

                }
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {

            val futureStudioIconFile: File =
                File(commonDocumentDirPath("estatePie"), File.separator + viewModel.fileName+".csv")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Timber.d("file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            false
        }
    }

    fun commonDocumentDirPath(FolderName: String): File? {
        var dir: File? = null
        dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + "/" + FolderName
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString() + "/" + FolderName)
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            val success = dir.mkdirs()
            if (!success) {
                dir = null
            }
        }
        return dir
    }

}