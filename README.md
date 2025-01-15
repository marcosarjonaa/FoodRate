# Marcos Arjona Comino  
## PMyDM 2ºDAM A
### IES Virgen del Carmen  
---

# FoodRate  

**¿Qué es Foodrate?**

Foodrate es una aplicacion para ayudarte a tener muchas recetas o restaurantes para que tengas más opciones para elegir que comer cuando estes dudoso, prefieras cocinar algo distinto, entre otros usos. 

Para ello usamos un listado de recetas modificable que hace que puedas añadir, borrar o editar recetas para que tengas mayor facilidad a la hora de organizarte en tus recetas. Si lo tuyo tambiés es comer en restaurantes, FoodRate también es para ti, tenemos un listado de restaurantes con mucha utilidad para recordar tu experiencia en ellos platos comidos y mas allá de lo escrito, también puedes guardar fotos del lugar.

Espero que mi trabajo sea de tu agrado y te ayude, Marcos Arjona Comino

---
## Las activitys
### MainActivity:  
#### Métodos:  
- **onCreate:** Crea e infla el activity a través del binding Main, así como tambien crea el navcontroller el toolbar y redirige al tocar en las opciones que se necesiten, en este caso al inicio, al listado de recetas, de restaurantes, la configuración y el login.
- **onCreateOptionsMenu**: infla el menu lateral.
- **onSupportNavigateUp**: permite la navigación de desplazamiento.
- **onOptionsItemSelected**: tiene las opciones de los 3 puntitos que se guardan en el toolbar. 
- **initSharedPreferences:** Gestiona las preferencias compartidas.
- **logout:** Cierra la sesión.

#### Código
``` kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var ficheroCompartido: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciarPreferenciasCompartidas()
        auth = FirebaseAuth.getInstance()
        val toolbar = binding.appBarConfiguration.toolbar
        setSupportActionBar(toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragmentComidas, R.id.fragmentInicio, R.id.fragmentRestaurantes, R.id.fragmentConfiguration),
            binding.main
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.myNavView.setupWithNavController(navController)
        binding.myNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fragmentInicio -> {
                    navController.navigate(R.id.fragmentInicio)
                    true
                }
                R.id.fragmentComidas -> {
                    navController.navigate(R.id.fragmentComidas)
                    true
                }
                R.id.fragmentRestaurantes -> {
                    navController.navigate(R.id.fragmentRestaurantes)
                    true
                }
                R.id.fragmentConfiguration -> {
                    navController.navigate(R.id.fragmentConfiguration)
                    true
                }
                R.id.login -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragmentInicio -> {
                navController.navigate(R.id.fragmentInicio)
                true
            }
            R.id.fragmentConfiguration -> {
                navController.navigate(R.id.fragmentConfiguration)
                true
            }
            R.id.login -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun iniciarPreferenciasCompartidas() {
        val nombreFicheroCompartido = getString(R.string.ficheroCompartido)
        ficheroCompartido = getSharedPreferences(nombreFicheroCompartido, MODE_PRIVATE)
    }

    private fun logout() {
        auth.signOut()
        val loginIntent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(loginIntent)
    }
}
```

---

### LoginActivity  
#### Métodos: 
- **onCreate** lo mismo que en el código anterior
- **init:** inicializa todos los botones
- **recoverPassword:** manda el email para recuperar la contraseña-
- **startlogin:** compruba definitivamente la conexion a firebase
- **navigateToMainActivity:** navega al main activity 
- **start:** recoge todos los para comprobar los campos de correo y contraseña  para iniciar sesion
- **estaLogueado:** comprueba si está logueado
- **salvarLogueo:** comprueba si hay datos guardados para cuando el login se queda guardado

#### Código: 
``` kotlin
class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnRecoverPass: Button
    private lateinit var editUser: EditText
    private lateinit var editPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        if (estaLogueado()) {
            navigateToMainActivity()
            finish()
        }
        init()
        start()
    }

    private fun init() {
        btnLogin = findViewById(R.id.btn_login_in_login)
        btnRegister = findViewById(R.id.btn_register_in_login)
        btnRecoverPass = findViewById(R.id.btn_recover_pass)
        editUser = findViewById(R.id.edit_user_login)
        editPassword = findViewById(R.id.edit_pass_login)
    }

    private fun start() {
        btnLogin.setOnClickListener {
            val user = editUser.text.toString()
            val pass = editPassword.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty())
                startLogin(user, pass) { result, msg ->
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    if (result) {
                        salvarLogueo(user) // Save login state
                        navigateToMainActivity()
                        finish()
                    }
                }
            else
                Toast.makeText(this, "Tienes algún campo vacío", Toast.LENGTH_LONG).show()
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        btnRecoverPass.setOnClickListener {
            val user = editUser.text.toString()
            if (user.isNotEmpty())
                recoverPassword(user) { result, msg ->
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    if (!result) editUser.setText("")
                }
            else
                Toast.makeText(this, "Debes rellenar el campo email", Toast.LENGTH_LONG).show()
        }
    }

    private fun recoverPassword(email: String, onResult: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { taskResetEmail ->
                if (taskResetEmail.isSuccessful) {
                    onResult(true, "Acabamos de enviarte un email con la nueva password")
                } else {
                    val msg = try {
                        throw taskResetEmail.exception ?: Exception("Error de reseteo inesperado")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        "El formato del email es incorrecto"
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    private fun startLogin(user: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener { taskAssin ->
                if (taskAssin.isSuccessful) {
                    val posibleUser = auth.currentUser
                    if (posibleUser?.isEmailVerified == true) {
                        onResult(true, "Usuario Logueado satisfactoriamente")
                    } else {
                        auth.signOut()
                        onResult(false, "Debes verificar tu correo antes de loguearte")
                    }
                } else {
                    val msg = try {
                        throw taskAssin.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        "El usuario tiene problemas por haberse borrado o desabilitado"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        if (e.message?.contains("There is no user record corresponding to this identifier") == true) {
                            "El usuario no existe"
                        } else "Contraseña incorrecta"
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    private fun salvarLogueo(email: String) {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    private fun estaLogueado(): Boolean {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
```

---
### RegistrarActivty
#### Métodos
- **onCreate.**
- **init.**
- **start:** comprueba que los valores sean distintos y sino, los registra
- **startActivityLogin:** te manda al activity de login
- **registerUser:** crea el usuario y lanza excepciones en caso que sean necesario

#### Código
``` kotlin
class RegistrarActivity : AppCompatActivity() {
    private lateinit var btnRegister: Button
    private lateinit var btnLastRegister: Button
    private lateinit var editUser: EditText
    private lateinit var editPassword: EditText
    private lateinit var editRepeatPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        start()
    }

    private fun init() {
        btnRegister = findViewById(R.id.btn_register_in_register)
        btnLastRegister = findViewById(R.id.btn_last_register)
        editUser = findViewById(R.id.edit_user_register)
        editPassword = findViewById(R.id.edit_pass_register)
        editRepeatPassword = findViewById(R.id.pass_register_repeat_in_register)

        auth = Firebase.auth

    }


    private fun start() {
        btnRegister.setOnClickListener {
            val email = editUser.text.toString()
            val pass = editPassword.text.toString()
            val repeatPass = editRepeatPassword.text.toString()
            if (pass != repeatPass
                || email.isEmpty()
                || pass.isEmpty()
                || repeatPass.isEmpty()
            )
                Toast.makeText(this, "Campos vacíos y/o password diferentes", Toast.LENGTH_LONG)
                    .show()
            else {
                registerUser(email, pass) { result, msg ->
                    Toast.makeText(this@RegistrarActivity, msg, Toast.LENGTH_LONG).show()
                    if (result)
                        startActivityLogin()
                }
            }
        }

        btnLastRegister.setOnClickListener {
            // Como la lambda tiene un parámetro view, el this es el Activity
                view ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startActivityLogin() {
        //Tengo que lanzar un intent con el Activity a loguear.
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() //No quiero que sigua el Activity del registro.

    }

    private fun registerUser(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { taskAssin ->
                if (taskAssin.isSuccessful) {
                    //enviaremos un email de confirmación
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { taskVerification ->
                            var msg = ""
                            if (taskVerification.isSuccessful)
                                msg = "Usuario Registrado Ok. Verifique su correo"
                            else
                                msg =
                                    "Usuario Registrado Ok. ${taskVerification.exception?.message}"
                            auth.signOut() //tiene que verificar antes el email
                            onResult(true, msg)
                        }
                        ?.addOnFailureListener { exception ->
                            Log.e(
                                "ActivityRegister",
                                "Fallo al enviar correo de verificación: ${exception.message}"
                            )
                            onResult(
                                false,
                                "No se pudo enviar el correo de verificación: ${exception.message}"
                            )
                        }

                } else {
                    try {
                        throw taskAssin.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        onResult(false, "Ese usuario ya existe")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        onResult(false, "La contraseña es débil: ${e.reason}")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        onResult(false, "El email proporcionado, no es válido")
                    } catch (e: Exception) {
                        onResult(false, e.message.toString())
                    }

                }
            }
```

---

## Fragmentos
 - #### FragmentComidas
 - #### FragmentDetalles
 - #### FragmentRestaurantes
 - #### FragmentInicio
 - #### FragmentConfiguracion

### FragmentoComidas
#### Métodos
 - onCreateView: Inicializa las diferentes variables

#### Código
```kotlin
class FragmentComidas() : Fragment() {
    lateinit var binding: FragmentComidasBinding
    lateinit var controller : Controller
    lateinit var activityContext : Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComidasBinding.inflate(inflater, container, false)
        activityContext = requireActivity()
        controller = Controller(activityContext, this)
        controller.initdata()
        return binding.root
    }

}
```

---

### Fragmento Detalles
- **onCreateView:** para el binding
- **onViewCreated:** pone los detalles en el binding

#### Código
```kotlin
class FragmentDetalles : Fragment() {
    private lateinit var binding: FragmentDetallesBinding
    private val args: FragmentDetallesArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetallesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idItem = args.idItem

        val recetas = DaoRecetas.myDao.getDataRecetas()[idItem]

        binding.nombre.text = recetas.name
        binding.description.text = recetas.descripcion
        Glide.with(this)
            .load(recetas.image)
            .centerCrop()
            .into(binding.imagen)
    }
}
```
---

### Fragmento Restaurantes
#### Métodos
- **onCreateView.**
- **initData:** carga los datos
- **setAdapter:** setea el adapter
- **onDestroyView:** destruye la vista

#### Código
```kotlin
  class FragmentRestaurantes : Fragment() {
    lateinit var binding: FragmentRestaurantesBinding
    private lateinit var listRestaurantes: MutableList<Restaurantes>
    lateinit var adapterAnuncio: AdapterRestaurantes
    private var layoutManagerRestaurantes: LinearLayoutManager = LinearLayoutManager(context)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantesBinding.inflate(inflater, container, false)
        initData()
        return binding.root
    }

    fun initData(){
        listRestaurantes = DaoRestaurantes.myDao.getDataAnuncios().toMutableList()
        setAdapter()

    }
    fun setAdapter() {
        adapterAnuncio = AdapterRestaurantes(listRestaurantes)
        binding.restaurantes.layoutManager = layoutManagerRestaurantes
        binding.restaurantes.adapter = adapterAnuncio
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.restaurantes.adapter = null
        binding.restaurantes.layoutManager = null
    }
}
```
---

### Fragmento Inicio

#### Métodos
 - **onCreate**
 - **onViewCreated** 

#### Código

```kotlin
class FragmentInicio : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }
}
```

---

### Fragmento Configuración

#### Métodos
 - **onCreate**
 - **onCreateView**

#### Código
```kotlin
class FragmentConfiguration : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

}

```


---
## Carpeta Adapter
- ##### **AdapterRecetas**
- ##### **ViewHRecetas**
- ##### **AdapterRestaurantes**
- ##### **ViewRestaurantes**

  
### AdapterRecetas
#### Métodos
- **onCreate.**
- **onBindViewHolder:** renderiza el viewholder
- **getItemCount:** recibe el tamaño de la lista de recetas

#### Código
```kotlin
class AdapterRecetas(
    var listRecetas: MutableList<Recetas>,
    var deleteClick: (Int) -> Unit,
    var updateClick: (Int) -> Unit
    ) : RecyclerView.Adapter<ViewHRecetas>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHRecetas {
            val layoutInflater = LayoutInflater.from(parent.context)
            val layoutItemRecetas = R.layout.activity_cardview
            return ViewHRecetas(layoutInflater.inflate(layoutItemRecetas, parent, false), deleteClick, updateClick)
        }

        override fun onBindViewHolder(holder: ViewHRecetas, position: Int) {
            holder.renderize(listRecetas[position], position)
        }

        override fun getItemCount(): Int = listRecetas.size


}
```

---
### ViewHRecetas
#### Métodos
- **init:** iniciar el binding
- **renderize:** renderiza los textos de las recetas.
- **setOnClickListener:** escucha para ver si han clicado  en delete  o update. 

#### Código
``` kotlin
class ViewHRecetas (
    view: View,
    var deleteOnClick: (Int) -> Unit,
    var updateOnClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder (view){
    var binding: ActivityCardviewBinding

    init {
        binding = ActivityCardviewBinding.bind(view)
    }

    fun renderize(recetas: Recetas, position: Int){
        binding.txtNombre.setText(recetas.name)
        binding.txtDescripcion.setText(recetas.descripcion)
        binding.txtNota.setText(recetas.nota)
        Glide
            .with(itemView.context)
            .load(recetas.image)
            .centerCrop()
            .into(binding.ivRecetas)
        setOnClickListener(position)
    }

    private fun setOnClickListener(position : Int) {
        binding.ivDelete.setOnClickListener {
            deleteOnClick(position)
        }
        binding.ivEdit.setOnClickListener {
            updateOnClick(position)
        }
    }
}
```

---
### Adapter Restaurantes
#### Métodos
- **onCreateViewHolder:** infla los layout
- **onBindViewHolder:** renderiza el viewholder
- **getItemCount:** recibe el tamaño de la lista de restaurantes

#### Código
```kotlin
class AdapterRestaurantes (var listRestaurantes: MutableList<Restaurantes>) : RecyclerView.Adapter<ViewRestaurantes>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewRestaurantes {
            val layoutInflater = LayoutInflater.from(parent.context)
            val layoutItemRestaurantes = R.layout.fragmentviewrestaurante
            return ViewRestaurantes(layoutInflater.inflate(layoutItemRestaurantes, parent, false))
        }

        override fun getItemCount(): Int  = listRestaurantes.size

        override fun onBindViewHolder(holder: ViewRestaurantes, position: Int) {
            holder.renderize(listRestaurantes[position])
        }
}
```

--- 
### ViewRestaurantes
#### Métodos
- **init:** iniciar el binding
- **renderize:** renderiza los textos de las recetas.

#### Código
``` kotlin
class ViewRestaurantes ( view: View) : RecyclerView.ViewHolder(view) {
    var binding: FragmentviewrestauranteBinding
    init {
        binding = FragmentviewrestauranteBinding.bind(view)
    }
    fun renderize(restaurantes: Restaurantes) {
        binding.nombre.setText(restaurantes.nombre)
        binding.lugar .setText(restaurantes.lugar)
        Glide
            .with(itemView.context)
            .load(restaurantes.imagen)
            .centerCrop()
            .into(binding.imagen)


    }
}
```

---
## Carpeta Controller
- ##### Controller

### Controller
#### Métodos
- **initdata**
- **logOut**
- **setAdapter:** carga el adapter con métodos posteriormente explicados
  - **deleteRecetas:** método que se manda al adapter encargado de eliminar una receta segun su posición
  - **updateRecetas:** método que se manda al adapter encargado de lanzar el dialogo de editar
- **initOnClickListener:** carga el contexto del MainActivity
- **okOnEditReceta:** confirma el cambio de datos, eliminando el item que habia en esa posición y cambiandolo por el nuevo
- **addReceta:** añade una receta lanzando su correspondiente dialog
- **okOnNewReceta:** confirma el añadido de datos que necesita el item 
- **navigateToDetails:** navega a la pestaña de detalles

#### Código:
``` kotlin
class Controller (val contextoActivity: Context, val fragment: FragmentComidas) {
    private val context = fragment.requireContext();
    lateinit var listRecetas : MutableList<Recetas>
    private lateinit var adapter: AdapterRecetas
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(context)

    fun initdata(){
        listRecetas = DaoRecetas.myDao.getDataRecetas().toMutableList()
        setAdapter()
        initOnClickListener()
    }

    private fun navigateToDetails(pos: Int) {
        fragment.findNavController().navigate(
            FragmentComidasDirections.actionFragmentComidasToFragmentDetalles(
                idItem = Integer.parseInt(listRecetas[pos].id)
            )
        )
    }

    fun setAdapter(){
        adapter = AdapterRecetas(
            listRecetas,
            { pos -> deleteRecetas(pos) },
            { pos -> updateRecetas(pos) },
            { pos -> navigateToDetails(pos) }
    )
        fragment.binding.myRecyclerView.layoutManager = layoutManager
        fragment.binding.myRecyclerView.adapter = adapter
    }

    fun deleteRecetas(posicion : Int){
        val myActivity = context as MainActivity
        val dialogDelete = DialogDeleteRecetas(posicion){
            if(posicion in listRecetas.indices){
                listRecetas.removeAt(posicion)
                fragment.binding.myRecyclerView.adapter?.apply{
                    notifyItemRemoved(posicion)
                    notifyItemRangeChanged(posicion, listRecetas.size - posicion)
                }
                Toast.makeText(context, "Se eliminó la receta en la posicion $posicion", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(context, "Índice fuera de rango: $posicion", Toast.LENGTH_LONG).show()
            }
        }
        dialogDelete.show(myActivity.supportFragmentManager, "Borro una receta")
    }

    private fun initOnClickListener() {
        fragment.binding.btnAdd.setOnClickListener{
            addReceta()
        }
    }

    fun updateRecetas(pos: Int) {
        val editDialog = DialogEditRecetas(listRecetas.get(pos)){
            editReceta -> okOnEditReceta(editReceta, pos)
        }
        val myActivity = context as MainActivity
        editDialog.show(myActivity.supportFragmentManager, "Edito una receta")
    }

    private fun okOnEditReceta(editRecetas: Recetas, pos: Int){
        listRecetas.removeAt(pos);
        adapter.notifyItemRemoved(pos);
        listRecetas.add(pos, editRecetas);
        adapter.notifyItemInserted(pos);

        fragment.binding.myRecyclerView.post {
            layoutManager.scrollToPositionWithOffset(pos, 20)
        }
    }

    fun addReceta() {
        Toast.makeText(context, "Añadimos una receta", Toast.LENGTH_LONG).show()
        val dialog = DialogAddRecetas { receta -> okOnNewReceta(receta)}
        val myActivity = context as MainActivity;
        dialog.show(myActivity.supportFragmentManager, "Añado una nueva receta");
    }

    private fun okOnNewReceta(receta: Recetas){
        Log.d("Controler", "Añadiendo receta: $receta");
        listRecetas.add(listRecetas.size, receta);
        adapter.notifyItemInserted(listRecetas.lastIndex)
        val myActivity = context as MainActivity;
        fragment.binding.myRecyclerView.post{
            layoutManager.scrollToPositionWithOffset(listRecetas.size, 25)
        }
    }
}
```
---

## Carpeta Interfaces
- ##### **InterfaceDao**
- ##### **InterfaceDaoRestaurantes**


### InterfaceDao
#### Métodos
- **getDataRecetas**

#### Código
``` kotlin
interface InterfaceDao {
    fun getDataRecetas(): List<Recetas>
}
```
---

### InterfaceDaorestaurantes
#### Métodos
- **getDataRestaurantes**

#### Código
``` kotlin
interface InterfaceDaoRestaurantes {
    fun getDataRestaurantes(): List<Restaurantes>
}
```
---

## Carpeta DAO
- ##### **DaoRecetas**
- ##### **DaoRestaurantes**

### DaoRecetas
#### Métodos
- **companion object:** uso del byLazy
- **getDataRecetas:** Lanza el repositorio de Lista de recetas

#### Código
``` kotlin
class DaoRecetas private constructor() : InterfaceDao {
    companion object{
        val myDao: DaoRecetas by lazy {
            DaoRecetas()
        }
    }

    override
    fun getDataRecetas(): List<Recetas>  = Repositorio.ListaRecetas
}
```
---
### DaoRestaurantes
#### Métodos
- **companion object:** uso del byLazy
- **getDataRecetas:** Lanza el repositorio de Lista de restaurantes

#### Código
``` kotlin
class DaoRestaurantes private constructor(): InterfaceDaoRestaurantes {
        companion object {
            val myDao: DaoRestaurantes by lazy {
                DaoRestaurantes()
            }
        }

        override fun getDataRestaurantes(): List<Restaurantes> = RestaurantesLista.ListaRestaurantes
}
```
---

## Carpeta Models
- ##### **Recetas**
- ##### **Restaurantes**

### Recetas
#### Métodos
- **toString:** convierte a string los parámetros necesarios de name, descripcion, nota e image.

#### Códgo: 
``` kotlin
class Recetas(
    var name: String,
    var descripcion: String,
    var nota: String,
    var image: String
) {
    override fun toString(): String {
        return "Recetas(name='$name', descripcion'$descripcion',nota='$nota', image='$image')"
    }
}
```

--- 
### Restaurantes
#### Métodos
- **toString:** convierte a string los parámetros necesarios de name, descripcion, nota e image.

#### Códgo: 
``` kotlin
class Restaurantes(
    var nombre: String,
    var lugar: String,
    var imagen: String
) {
    override fun toString(): String {
        return "Restaurantes(name='$nombre', lugar'$lugar', image='$imagen')"
    }
}
```
--- 

## Carpeta Object_Models
- ##### ArgumentsRecetas
- ##### Recetas
- ##### RestaurantesListas

###  ArgumentsRecetas
#### Código
``` kotlin
object ArgumentsRecetas {
    const val ARGUMENT_NAME = "name"
    const val ARGUMENT_DESCRIPCION = "descripcion"
    const val ARGUMENT_NOTA = "nota"
    const val ARGUMENT_IMAGE = "image"
}
```
---
### Repositorio
#### Código
``` kotlin
object Repositorio {
    val ListaRecetas : List<Recetas> = listOf(
        Recetas(
            "Bolos lucentinos",
            "1",
            "Los bolos lucentinos son una comida que ha pasado generación tras generacion" +
                    " de abuelas a madres. Típica de Lucena, es un plato que une la carne y las almendras.",
            "8",
            "https://cocinandoentreolivos.com/wp-content/uploads/2017/11/Bolos-Lucentinos-tradicionales-Lucena-www.cocinandoentreolivos.com-30.jpg"
        ),
        ...
    )
}
```
--- 
### RestaurantesLista
#### Código
``` kotlin
object RestaurantesLista {
    val ListaRestaurantes : List<Restaurantes> = listOf(
        Restaurantes(
            "Las tres culturas",
            "Lucena, c/Heredia, 2",
            "https://tresculturasrestaurante.com/wp-content/uploads/2020/05/cropped-a.jpg"
        ),
        ...
    )
}
```
--- 

## Carpeta Dialogues
- ##### DialogAddRecetas
- ##### DialogEditRecetas
- ##### DialogDeletaRecetas

### DialogAddRecetas
#### Métodos
- **onCreateDialog:** devuelve el activity donde se meten los datos necesarios para crear una nueva recetas
- **validacion:** comprueba que los campos del dialog no sean empty
- **recoverDataLayout:** carga el binding para recoger datos 

#### Código
``` kotlin
class DialogAddRecetas (
    private val onNewRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { myActivity ->
            val builder = AlertDialog.Builder(myActivity);
            val inflater = myActivity.layoutInflater;
            val viewDialog = inflater.inflate(R.layout.dialog_add_recetas, null)
            builder.setView(viewDialog)
            builder.setMessage("Añadir una receta")
                .setPositiveButton("Crear",
                    DialogInterface.OnClickListener{
                        dialog, id ->
                        val receta = recoverDataLayout(viewDialog)
                        if(validacion(receta)){
                            onNewRecetasDialog(receta);
                            Toast.makeText(myActivity, "receta creada", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(myActivity, "Llena todos los campos", Toast.LENGTH_LONG).show()
                        }
                    }
                ).setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener{dialog, id ->
                        Toast.makeText(myActivity, "Has cerrado la ventana de añadir", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    ).create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

    private fun validacion(receta: Recetas): Boolean {
        return receta.name.isNotEmpty() &&
                receta.id.toString().isNotEmpty() &&
                receta.descripcion.isNotEmpty() &&
                receta.nota.isNotEmpty() &&
                receta.image.isNotEmpty()
    }

    private fun recoverDataLayout(view: View): Recetas {
        val binding = DialogAddRecetasBinding.bind(view)
        return Recetas(
            binding.etName.text.toString(),
            binding.etId.text.toString(),
            binding.etDescripcion.text.toString(),
            binding.etNota.text.toString(),
            binding.etImage.text.toString()
        )
    }
}
```
---
### DialogEditRecetas
#### Métodos
- **init:** recupera los valores de strings
- **onCreateDialog:** devuelve el activity donde se meten los datos necesarios para editar la receta cargando los datos
- **recoverDataLayout:** carga el binding para recoger datos 

#### Código
``` kotlin
class DialogEditRecetas (
    val recetasToUpdate: Recetas,
    val updateRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    private val ARGUMENT_NAME : String = ArgumentsRecetas.ARGUMENT_NAME
    private val ARGUMENT_ID : String = ArgumentsRecetas.ARGUMENT_ID
    private val ARGUMENT_DESCRIPCION : String = ArgumentsRecetas.ARGUMENT_DESCRIPCION
    private val ARGUMENT_NOTA : String = ArgumentsRecetas.ARGUMENT_NOTA
    private val ARGUMENT_IMAGE : String = ArgumentsRecetas.ARGUMENT_IMAGE
    init{
        val args = Bundle().apply {
            putString(ARGUMENT_NAME, recetasToUpdate.name)
            putString(ARGUMENT_ID, recetasToUpdate.id)
            putString(ARGUMENT_DESCRIPCION, recetasToUpdate.descripcion)
            putString(ARGUMENT_NOTA, recetasToUpdate.nota)
            putString(ARGUMENT_IMAGE, recetasToUpdate.image)
        }
        this.arguments = args
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val inflater = requireActivity().layoutInflater
            val binding = DialogEditRecetasBinding.inflate(inflater)
            arguments?.let { args ->  // seteo los datos iniciales en los campos del dialogo
                binding.etName.setText(args.getString(ARGUMENT_NAME))
                binding.etId.setText(args.getString(ARGUMENT_ID))
                binding.etdescripcion.setText(args.getString(ARGUMENT_DESCRIPCION))
                binding.etNota.setText(args.getString(ARGUMENT_NOTA))
                binding.etImage.setText(args.getString(ARGUMENT_IMAGE))
            }
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(binding.root)
                .setTitle("Editar receta: ")
                .setPositiveButton("Aceptar") { dialog, id ->
                    val updateRecetas = recoverDataLayout(binding)

                    if (updateRecetas.name.isEmpty() ||
                        updateRecetas.id.isEmpty() ||
                        updateRecetas.descripcion.isEmpty() ||
                        updateRecetas.nota.isEmpty() ||
                        updateRecetas.image.isEmpty()
                    ) {
                        Toast.makeText(requireContext(), "Algún campo está vacío", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    } else {
                        updateRecetasDialog(updateRecetas)

                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

    private fun recoverDataLayout(binding: DialogEditRecetasBinding): Recetas {
        return Recetas(
            binding.etName.text.toString(),
            binding.etId.id.toString(),
            binding.etdescripcion.text.toString(),
            binding.etNota.text.toString(),
            binding.etImage.text.toString()
        )
```
---
### DialogDeleteRecetas
#### Métodos
- **init:** recupera los valores de strings
- **onCreateDialog:** devuelve el activity donde se pide la confirmacion de la voluntad de eliminar la receta

#### Código
``` kotlin
class DialogDeleteRecetas (
    val pos : Int,
    val onDeleteRecetasDialog: (Int) -> Unit
) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { myActivity ->
            val builder = AlertDialog.Builder(myActivity)
            val inflater = myActivity.layoutInflater
            val viewDialog = inflater.inflate(R.layout.dialog_delete_recetas, null)
            builder.setView(viewDialog)
            builder.setMessage("¿Quieres eliminar la receta?")
                .setPositiveButton("Eliminar") { dialog, id ->
                    onDeleteRecetasDialog(pos)
                }
                .setNegativeButton("Cancelar") { dialog, id ->
                    dialog.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }
}
```
--- 



# Hecho por Marcos Arjona Comino