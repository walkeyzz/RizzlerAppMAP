# EXPLAIN CODE PART 1
Sedikit penjelasan tentang isi kode kode yang dibuat, dikit aja yah, klo kebanyakan mager ngetik hehe.

Urutan Class yang di running :
1. MainActivity
2. SignUpActivity
3. HomeActivity
4. Other Activity 

# MainActivity
Pertama import package yang akan diperlukan dulu yah

```
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
```

Fungsi nya untuk berpindah layar, mengatur warna, memberi tau Android bahwa ini adalah halaman utama aplikasinya. mengirim dan menerima data antar komponen Android, mengatur penjadwalan tugas, untuk fungsi perulangan, dan untuk masuk ke firebase

```
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.TRANSPARENT

        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null)
                startActivity(Intent(this, SignUpActivity::class.java))
            else
                startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 3000)

    }
}
```
setContentView untuk ngasih tau layout mana yang dipakai di class ini, lalu status bar diubah warna ke transparan.
Lalu kita membuat objek Handler untuk execute tugas tertentu menggunakan postDelayed dan function if untuk memeriksa apakah user sudah login atau belum, jika belum maka akan berpindah layar ke halaman sign up, jika sudah login sebelumnya akan langsung direct ke hoome activity. Fungsi finish agar user tidak balik lagi ke main activity
3000 untuk menentukan penundaan waktu (dalam milidetik) sebelum tugas dijalankan. 

# SignUpActivity
Kayaknya kepanjangan klo masukin semua kode ya, jadi jelasin nya yang penting-penting aja yaa, full kode bisa liat di folder lain.

```
val binding by lazy {
    ActivitySignUpBinding.inflate(layoutInflater)
}
```
Ini digunakan untuk menginisialisasi variabel binding dengan menggunakan Lazy Delegation. ActivitySignUpBinding.inflate(layoutInflater) digunakan untuk menghubungkan layout XML dengan kelas ActivitySignUpBinding yang digenerate oleh View Binding.

```
lateinit var user: User
```
Ini adalah variabel user yang akan menyimpan informasi pengguna seperti nama, email, dan lainnya
```
private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    uri?.let {
        uploadImage(uri, USER_PROFILE_FOLDER){
            if (it!=null){
                user.image=it
                binding.profileImage.setImageURI(uri)
            }
        }
    }
}
```
Variabel launcher digunakan untuk mendaftarkan result contract untuk mendapatkan konten gambar (image) dari galeri atau penyimpanan perangkat. Saat pengguna memilih gambar, fungsi lambda akan dijalankan untuk mengunggah gambar ke folder profil pengguna dan menetapkan gambar tersebut pada ImageView (binding.profileImage).
```
val text = "<font color=#FF000000>Already have an Account</font> <font color=#1E88E5>Login ?</font>"
binding.login.setText(Html.fromHtml(text))
```
Kode ini menginisialisasi teks dengan HTML formatting, di mana teks "Already have an Account" akan memiliki warna hitam (#FF000000) dan "Login ?" akan memiliki warna biru. Kemudian, teks tersebut ditetapkan pada TextView (binding.login).

```
if (intent.hasExtra("MODE")){
    if (intent.getIntExtra("MODE",-1)==1){
        binding.signUpBtn.text="Update Profile"
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                user= it.toObject<User>()!!
                if (!user.image.isNullOrEmpty()){
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
                binding.name.editText?.setText(user.name)
                binding.email.editText?.setText(user.email)
                binding.password.editText?.setText(user.password)
            }
    }
}
```
Kode ini memeriksa apakah ada data tambahan ("MODE") yang dikirim melalui intent. Jika iya dan nilainya 1, maka dianggap sebagai mode pembaruan profil. Tombol pada layar akan diperbarui menjadi "Update Profile", dan data profil pengguna saat ini akan dimuat dari Firestore dan ditampilkan pada formulir pendaftaran.

```
binding.signUpBtn.setOnClickListener {
    if (intent.hasExtra("MODE")){
        if (intent.getIntExtra("MODE",-1)==1){
            Firebase.firestore.collection(USER_NODE)
                .document(Firebase.auth.currentUser!!.uid).set(user)
                .addOnSuccessListener {
                    startActivity(Intent(this@SignUpActivity,HomeActivity::class.java))
                    finish()
                }
        }
    }
    else{
        // ...
        // Kode ini menangani proses pendaftaran pengguna baru atau pembaruan profil.
        // ...
    }
}
```
Kode ini menetapkan OnClickListener pada tombol pendaftaran (binding.signUpBtn). Jika dalam mode pembaruan profil, data profil akan diperbarui di Firestore. Jika tidak, maka kode akan menangani proses pendaftaran pengguna baru atau pembaruan profil sesuai dengan isian formulir.

```
binding.addImage.setOnClickListener {
    launcher.launch("image/*")
}
```
Kode ini menetapkan OnClickListener pada ImageView (binding.addImage). Saat diklik, fungsi launcher.launch("image/*") akan dijalankan, memulai proses pemilihan gambar dari penyimpanan perangkat.

```
binding.login.setOnClickListener {
    startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
    finish()
}
```
Kode ini menetapkan OnClickListener pada TextView (binding.login). Saat diklik, akan memulai aktivitas LoginActivity dan menutup aktivitas SignUpActivity saat ini.

# LoginActivity
```
class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
```
Inflate layout menggunakan ActivityLoginBinding. Variabel binding diinisialisasi secara lazy, menghindari overhead saat aktivitas dibuat
```
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
```
Set tata letak aktivitas dengan menggunakan root dari binding

Menambahkan listener untuk tombol login
```
        binding.loginBtn.setOnClickListener {
            // Memeriksa apakah field email atau password kosong
            if (binding.email.editText?.text.toString().equals("") or
                binding.pass.editText?.text.toString().equals("")
            ) {
                // Menampilkan pesan jika ada field yang kosong
                Toast.makeText(
                    this@LoginActivity,
                    "Please fill all the details",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Membuat objek User dari nilai input email dan password
                var user = User(
                    binding.email.editText?.text.toString(),
                    binding.pass.editText?.text.toString()
                )
```
Melakukan proses login dengan Firebase Authentication
```
                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Jika login berhasil, pindah ke HomeActivity
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()  // Menutup aktivitas saat ini agar tidak dapat kembali lagi
                        } else {
                            // Jika terjadi kesalahan, tampilkan pesan kesalahan
                            Toast.makeText(
                                this@LoginActivity,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
```
 Menambahkan listener untuk text "Don't have an account? sign up?"
 ```
        binding.text.setOnClickListener {
            // Pindah ke SignUpActivity jika text di-klik
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()  // Menutup aktivitas saat ini agar tidak dapat kembali lagi
        }
    }
}
```

# HomeActivity
```
class HomeActivity : AppCompatActivity() {
    // Variabel binding diinisialisasi menggunakan view binding
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout menggunakan ActivityHomeBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi view BottomNavigationView dari binding
        val navView: BottomNavigationView = binding.navView

        // Inisialisasi NavController dari host fragment di layout
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        // Menyesuaikan BottomNavigationView dengan NavController
        navView.setupWithNavController(navController)
    }
}
```
navView.setupWithNavController(navController): Menyesuaikan BottomNavigationView dengan NavController agar navigasi terjadi sesuai dengan fragment yang ditampilkan.

# AccountSettings
```
class AccountSettings : AppCompatActivity() {
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan view binding untuk meng-inflate layout activity
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur listener pada tombol logOutBtn
        binding.logOutBtn.setOnClickListener {
            // Memulai aktivitas LoginActivity dan menutup aktivitas saat ini
            startActivity(Intent(this@AccountSettings, LoginActivity::class.java))
            finish()
        }
    }
}
```

Masuk ke Fragment File okai

# HomeFragment
```
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var postList = ArrayList<Post>()
    private lateinit var adapter: PostAdapter
    private var followList = ArrayList<User>()
    private lateinit var followAdapter: FollowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inisialisasi adapter untuk RecyclerView yang menampilkan daftar posting
        adapter = PostAdapter(requireContext(), postList)
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())
        binding.postRv.adapter = adapter

        // Inisialisasi adapter untuk RecyclerView yang menampilkan daftar pengguna yang diikuti
        followAdapter = FollowAdapter(requireContext(), followList)
        binding.followRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.followRv.adapter = followAdapter

        // Menetapkan opsi menu di toolbar
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)

        // Mengambil daftar pengguna yang diikuti dari Firebase Firestore
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get()
            .addOnSuccessListener {
                var tempList = ArrayList<User>()
                followList.clear()
                for (i in it.documents) {
                    var user: User = i.toObject<User>()!!
                    tempList.add(user)
                }
                followList.addAll(tempList)
                followAdapter.notifyDataSetChanged()
            }
```
Mengambil daftar posting dari Firebase Firestore
```
        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList = ArrayList<Post>()
            postList.clear()
            for (i in it.documents) {
                var post: Post = i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Menambahkan opsi menu ke toolbar
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
```
# AddFragment
```
class AddFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menggunakan view binding untuk meng-inflate layout fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)

        // Menetapkan listener pada tombol post
        binding.post.setOnClickListener {
            // Memulai aktivitas PostActivity menggunakan Intent dan menutup aktivitas saat ini
            activity?.startActivity(Intent(requireContext(), PostActivity::class.java))
            activity?.finish()
        }

        // Menetapkan listener pada tombol reel
        binding.reel.setOnClickListener {
            // Memulai aktivitas ReelsActivity menggunakan Intent
            activity?.startActivity(Intent(requireContext(), ReelsActivity::class.java))
        }

        return binding.root
    }

    companion object {
        // Companion object digunakan untuk mendeklarasikan properti atau metode yang dapat diakses
        // secara langsung dari kelas tanpa membuat instance objek kelas.
    }
}
```

kayaknya saya nyerah dikit ngasi explanation karena panjang bgt, more explanation ada di presentasi yaa
