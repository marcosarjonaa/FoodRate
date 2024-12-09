# Marcos Arjona Comino  
## PMyDM  
### IES Virgen del Carmen  

## FoodRate  

**Marcos Arjona Comino**  
**Enlace a GitHub:**  
[FoodRate Repository](https://github.com/marcosarjonaa/FoodRate)  

2ºDAM A  

**Multimedia y dispositivos móviles**

---

### MainActivity:  
En el código que se ve a continuación, podemos observar:  
1. **Creación del binding y del fichero compartido.**  
2. En el `onCreate`, se inicializa la variable `binding` y se llama a las funciones `initSharedPreferences` y `mostrarUsuCon`.  

#### Métodos destacados:  
- **initSharedPreferences:** Crea y gestiona las preferencias compartidas.  

---

### Activity Main  
En esta activity se realiza:  
- La creación de **CardViews**, a los cuales se les añade un **FrameLayout** para estructurarlos.  
- Su diseño incluye:
  - Dos párrafos de texto.
  - Una imagen que aporta contexto al contenido.  

#### Nota:  
Más allá de esto, no tiene nada especial que no haya hecho anteriormente.

---

### LoginActivity  
En este archivo encontramos:  
- Un inicio de programa en el `onCreate`.  
- Un **binding** que detecta cuando el botón de **"Entrar"** es presionado.  
  - Recupera el texto de los `EditText` para **usuario** y **contraseña**.  
  - Comprueba que los valores sean correctos.  
  - En caso contrario, imprime un mensaje de error.  

Su diseño incluye:  
- **EditText** para usuario y contraseña.  
- Una imagen decorativa.  
- Uso de **ConstraintLayout** para evitar desplazamientos de los elementos.  

#### Nota:  
No tiene características especiales adicionales.

---

# Explicación del Código


### **Propiedades:**

### **Métodos:**
  Crea una instancia de `ViewHRecetas` inflando el layout `activity_cardview`.
  

  Devuelve el tamaño de la lista.

---

Se encarga de vincular los datos de una receta a las vistas correspondientes del layout.

### **Propiedades:**

### **Métodos:**


---


### **Propiedades:**

### **Métodos:**
  Carga las recetas iniciales desde `DaoRecetas`.

  Muestra un mensaje y los datos de las recetas en consola.

  Configura el adaptador y el `RecyclerView` en `MainActivity`.

  Elimina una receta de la lista si el índice es válido:
  - Actualiza el `RecyclerView` mediante `notifyItemRemoved` y `notifyItemRangeChanged`.
  - Muestra un mensaje indicando el éxito o error de la operación.

---

Singleton que implementa `InterfaceDao` para gestionar la lista de recetas desde un repositorio.

### **Métodos:**
  Devuelve la lista estática `ListaRecetas` desde `Repositorio`.

---

Define un método:

---

Representa una receta con las siguientes propiedades:

### **Sobreescritura:**
  Devuelve una representación textual de la receta, útil para depuración.

---

## **Flujo General:**
1. El controlador (`Controller`) inicializa los datos con `DaoRecetas` y configura el adaptador.
2. El adaptador (`AdapterRecetas`) vincula la lista de recetas con el `RecyclerView`.
3. Los `ViewHRecetas` renderizan los datos de cada receta y manejan las interacciones.

---
