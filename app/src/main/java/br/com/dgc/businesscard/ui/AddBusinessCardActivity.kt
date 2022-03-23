package br.com.dgc.businesscard.ui



import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.dgc.businesscard.App
import br.com.dgc.businesscard.R
import br.com.dgc.businesscard.data.BusinessCard
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout


class AddBusinessCardActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }

    private var stringImage : String = ""

    private val register = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        it.data?.data?.let { it1 ->
            applicationContext.contentResolver.takePersistableUriPermission(
                it1,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        stringImage = it.data?.data.toString()
        findViewById<ImageView>(R.id.iv_imagem).setImageURI(it.data?.data)

    }

    companion object{
        private const val PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business_card)
        insertListener()
    }

    private fun insertListener(){
        findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.iv_imagem).setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            intent.flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                   register.launch(intent)
                }
            } else {
                register.launch(intent)
            }
        }

        findViewById<MaterialButton>(R.id.btn_confirm).setOnClickListener {
            val businessCard = BusinessCard(
                nome = findViewById<TextInputLayout>(R.id.til_nome).editText?.text.toString(),
                empresa = findViewById<TextInputLayout>(R.id.til_empresa).editText?.text.toString(),
                telefone = findViewById<TextInputLayout>(R.id.til_telefone).editText?.text.toString(),
                email = findViewById<TextInputLayout>(R.id.til_email).editText?.text.toString(),
                fundoPersonalizado = findViewById<TextInputLayout>(R.id.til_cor).editText?.text.toString(),
                imagem = stringImage
            )
            mainViewModel.insert(businessCard)
            Toast.makeText(this, R.string.label_show_success, Toast.LENGTH_SHORT).show()
        }
    }
}