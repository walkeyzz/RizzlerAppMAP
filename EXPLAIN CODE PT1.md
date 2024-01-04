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
# MyPostFragment
```
class MyPostFragment : Fragment() {
    private lateinit var binding: FragmentMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPostBinding.inflate(inflater, container, false)
        // Inisialisasi postList dan adapter
        var postList = ArrayList<Post>()
        var adapter = MyPostRvAdapter(requireContext(), postList)

        // Setel layout manager dan adapter untuk RecyclerView
        binding.rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter

        // Ambil data post khusus pengguna dari Firestore
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            // List sementara untuk menyimpan post yang diambil
            var tempList = arrayListOf<Post>()

            // Loop melalui dokumen Firestore
            for (i in it.documents) {
                // Konversi dokumen Firestore ke objek Post
                var post: Post = i.toObject<Post>()!!
                // Tambahkan post ke list sementara
                tempList.add(post)
            }

            // Tambahkan semua post dari list sementara ke dalam postList utama
            postList.addAll(tempList)
            // Beri tahu adapter tentang perubahan data
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {
    }
}
```
- Import Library dan Package: Mendeklarasikan impor paket dan library yang digunakan.
- Deklarasi Variabel: Mendeklarasikan variabel binding yang digunakan untuk menghubungkan dengan layout binding dan postList serta adapter untuk menampilkan data post.
- onCreate: Override metode onCreate yang dipanggil ketika fragment dibuat.
- onCreateView: Override metode onCreateView yang dipanggil untuk membuat tampilan fragment.
- Inflate Layout: Menggunakan binding untuk menghubungkan dengan layout fragment FragmentMyPostBinding.
- Inisialisasi Variabel: Menginisialisasi postList dan adapter.
- Set RecyclerView Layout Manager dan Adapter: Mengatur layout manager dan adapter untuk RecyclerView.
- Ambil Data dari Firestore: Mengambil data post khusus pengguna dari Firestore menggunakan Firebase Firestore API.
- Loop Melalui Dokumen Firestore: Melakukan loop melalui dokumen Firestore untuk mendapatkan data post.
- Konversi Firestore ke Objek Post: Mengkonversi data Firestore menjadi objek Post.
- Tambahkan ke List Sementara: Menambahkan objek Post ke dalam list sementara (tempList).
- Tambahkan ke PostList Utama: Menambahkan semua post dari list sementara ke dalam postList utama.
- Beritahu Adapter tentang Perubahan Data: Menggunakan adapter.notifyDataSetChanged() untuk memberi tahu adapter tentang perubahan data.
- Return Root View: Mengembalikan root view yang telah di-inflate.
- Companion Object: Bagian yang mendeklarasikan companion object, tetapi dalam contoh ini tidak diisi dengan implementasi tambahan.

# MyReelsFragment
```
class MyReelsFragment : Fragment() {
    private lateinit var binding: FragmentMyReelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyReelsBinding.inflate(inflater, container, false)
        
        // Inisialisasi reelList dan adapter
        var reelList = ArrayList<Reel>()
        var adapter = MyReelAdapter(requireContext(), reelList)
        
        // Setel layout manager dan adapter untuk RecyclerView
        binding.rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter

        // Ambil data reel khusus pengguna dari Firestore
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).get().addOnSuccessListener {
            // List sementara untuk menyimpan reel yang diambil
            var tempList = arrayListOf<Reel>()

            // Loop melalui dokumen Firestore
            for (i in it.documents) {
                // Konversi dokumen Firestore ke objek Reel
                var reel: Reel = i.toObject<Reel>()!!
                // Tambahkan reel ke list sementara
                tempList.add(reel)
            }

            // Tambahkan semua reel dari list sementara ke dalam reelList utama
            reelList.addAll(tempList)
            // Beri tahu adapter tentang perubahan data
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {
    }
}
```
- Import Library dan Package: Mendeklarasikan impor paket dan library yang digunakan.
- Deklarasi Variabel: Mendeklarasikan variabel binding yang digunakan untuk menghubungkan dengan layout binding dan reelList serta adapter untuk menampilkan data reel.
- onCreate: Override metode onCreate yang dipanggil ketika fragment dibuat.
- onCreateView: Override metode onCreateView yang dipanggil untuk membuat tampilan fragment.
- Inflate Layout: Menggunakan binding untuk menghubungkan dengan layout fragment FragmentMyReelsBinding.
- Inisialisasi Variabel: Menginisialisasi reelList dan adapter.
- Set RecyclerView Layout Manager dan Adapter: Mengatur layout manager dan adapter untuk RecyclerView.
- Ambil Data dari Firestore: Mengambil data reel khusus pengguna dari Firestore menggunakan Firebase Firestore API.
- Loop Melalui Dokumen Firestore: Melakukan loop melalui dokumen Firestore untuk mendapatkan data reel.
- Konversi Firestore ke Objek Reel: Mengkonversi data Firestore menjadi objek Reel.
- Tambahkan ke List Sementara: Menambahkan objek Reel ke dalam list sementara (tempList).
- Tambahkan ke ReelList Utama: Menambahkan semua reel dari list sementara ke dalam reelList utama.
- Beritahu Adapter tentang Perubahan Data: Menggunakan adapter.notifyDataSetChanged() untuk memberi tahu adapter tentang perubahan data.
- Return Root View: Mengembalikan root view yang telah di-inflate.
- Companion Object: Bagian yang mendeklarasikan companion object, tetapi dalam contoh ini tidak diisi dengan implementasi tambahan.

# ProfileFragment
```
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set up click listener for edit profile button
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, AccountSettings::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
            activity?.finish()
        }

        // Set up ViewPagerAdapter for handling fragment navigation
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(), "My Post")
        viewPagerAdapter.addFragments(MyReelsFragment(), "My Reels")
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }

    companion object {
    }

    override fun onStart() {
        super.onStart()

        // Load user data from Firestore when the fragment starts
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                // Convert Firestore document to User object
                val user: User = it.toObject<User>()!!

                // Set user data to the corresponding views in the layout
                binding.name.text = user.name
                binding.bio.text = user.email

                // Load user image using Picasso library if available
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
            }
    }
}
```
- Deklarasi Variabel: Mendeklarasikan variabel binding yang digunakan untuk menghubungkan dengan layout binding dan viewPagerAdapter untuk menangani navigasi antar fragment.
- onCreate: Override metode onCreate yang dipanggil ketika fragment dibuat.
- onCreateView: Override metode onCreateView yang dipanggil untuk membuat tampilan fragment.
- Inflate Layout: Menggunakan binding untuk menghubungkan dengan layout fragment FragmentProfileBinding.
- Set Click Listener for Edit Profile Button: Menambahkan click listener untuk tombol edit profile untuk membuka halaman pengaturan akun.
- Set Up ViewPagerAdapter: Menginisialisasi dan menyiapkan ViewPagerAdapter untuk menangani navigasi antar fragment.
- Return Root View: Mengembalikan root view yang telah di-inflate.
- onStart: Override metode onStart yang dipanggil ketika fragment mulai aktif.
- Load User Data from Firestore: Mengambil data pengguna dari Firestore saat fragment mulai aktif.
- Convert Firestore to User Object: Mengkonversi dokumen Firestore menjadi objek User.
- Set User Data to Views: Mengatur data pengguna ke tampilan yang sesuai dalam layout.
- Load User Image Using Picasso: Menggunakan library Picasso untuk memuat gambar pengguna jika tersedia.

# ReelFragment
```
class ReelFragment : Fragment() {
    private lateinit var binding: FragmentReelBinding
    lateinit var adapter: ReelAdapter
    var reelList = ArrayList<Reel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan dengan layout FragmentReelBinding
        binding = FragmentReelBinding.inflate(inflater, container, false)
        
        // Inisialisasi adapter ReelAdapter dan mengaturnya ke ViewPager
        adapter = ReelAdapter(requireContext(), reelList)
        binding.viewPager.adapter = adapter

        // Mengambil data reel dari Firestore
        Firebase.firestore.collection(REEL).get().addOnSuccessListener {
            var tempList = ArrayList<Reel>()
            reelList.clear()

            // Mengkonversi dokumen Firestore menjadi objek Reel
            for (i in it.documents) {
                var reel = i.toObject<Reel>()!!
                tempList.add(reel)
            }

            // Membalik urutan list untuk menampilkan reel terbaru terlebih dahulu
            reelList.addAll(tempList.reversed())
            
            // Memberi tahu adapter bahwa data telah berubah dan perlu di-refresh
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {
    }
}
```
- Deklarasi Variabel: Mendeklarasikan variabel binding yang digunakan untuk menghubungkan dengan layout binding, adapter untuk adapter Reel, dan reelList sebagai ArrayList yang berisi objek Reel.
- onCreate: Override metode onCreate yang dipanggil ketika fragment dibuat.
- onCreateView: Override metode onCreateView yang dipanggil untuk membuat tampilan fragment.
- Inflate Layout: Menggunakan binding untuk menghubungkan dengan layout fragment FragmentReelBinding.
- Inisialisasi Adapter: Menginisialisasi adapter ReelAdapter dan mengaturnya ke ViewPager.
- Fetch Reel Data from Firestore: Mengambil data reel dari Firestore menggunakan Firebase Firestore.
- Convert Firestore Documents to Reel Objects: Mengkonversi dokumen Firestore menjadi objek Reel.
- Reverse the List: Membalik urutan list untuk menampilkan reel terbaru terlebih dahulu.
- Notify Adapter Changes: Memberi tahu adapter bahwa data telah berubah dan perlu di-refresh.

# SearchFragment
```
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan dengan layout FragmentSearchBinding
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        
        // Mengatur layout manager dan adapter untuk RecyclerView
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.rv.adapter = adapter

        // Mengambil data pengguna dari Firestore
        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {

            var tempList = ArrayList<User>()
            userList.clear()
            
            // Mengisi list pengguna dengan data dari Firestore
            for (i in it.documents) {
                // Memeriksa apakah pengguna adalah pengguna saat ini
                if (i.id != Firebase.auth.currentUser?.uid) {
                    var user: User = i.toObject<User>()!!
                    tempList.add(user)
                }
            }

            // Menambahkan data ke userList dan memberitahu adapter bahwa data telah berubah
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        // Menambahkan listener untuk tombol pencarian
        binding.searchButton.setOnClickListener {
            var text = binding.searchView.text.toString()

            // Mencari pengguna berdasarkan nama di Firestore
            Firebase.firestore.collection(USER_NODE).whereEqualTo("name", text).get()
                .addOnSuccessListener {

                    var tempList = ArrayList<User>()
                    userList.clear()

                    // Mengisi list pengguna dengan hasil pencarian
                    for (i in it.documents) {
                        // Memeriksa apakah pengguna adalah pengguna saat ini
                        if (i.id != Firebase.auth.currentUser?.uid) {
                            var user: User = i.toObject<User>()!!
                            tempList.add(user)
                        }
                    }

                    // Menambahkan data ke userList dan memberitahu adapter bahwa data telah berubah
                    userList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }
        }

        return binding.root
    }

    companion object {
    }
}
```
- Deklarasi Variabel: Mendeklarasikan variabel binding yang digunakan untuk menghubungkan dengan layout binding, adapter untuk adapter SearchAdapter, dan userList sebagai ArrayList yang berisi objek User.
- onCreate: Override metode onCreate yang dipanggil ketika fragment dibuat.
- onCreateView: Override metode onCreateView yang dipanggil untuk membuat tampilan fragment.
- Inflate Layout: Menggunakan binding untuk menghubungkan dengan layout fragment FragmentSearchBinding.
- Inisialisasi RecyclerView: Mengatur layout manager dan adapter untuk RecyclerView.
- Fetch User Data from Firestore: Mengambil data pengguna dari Firestore.
- Search Button Listener: Menambahkan listener untuk tombol pencarian.
- Search Users in Firestore: Mencari pengguna berdasarkan nama di Firestore dan mengupdate tampilan dengan hasil pencarian.
