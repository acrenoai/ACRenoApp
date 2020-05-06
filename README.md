# ACReno Auto Servis Application
Open source "ACReno" aplikacija za vođenje tekućih poslova oko evidencije 
popravki i održavanja automobila.

#### System Requirements

---
- **JAVA JDK 14**
- **JAVAFX 14**
- **MS ACCESS** 2019 (because is DB currently implemented in MS ACCESS)

#### Korisne komande

---
Add VM options for run JavaFX app on compile runtime:

```java
--module-path /PATH/TO/YOUR/JAVA/FX/javafx-sdk-14/lib 
                                        --add-modules javafx.controls,javafx.fxml
```


Where is '/PATH/.../.../FX/javafx-sdk-14/lib'  your JavaFx lib directory.

---
Run App command
```bash
java -jar --module-path {%PATH_TO_YOUR_FX_LIB_DIR}}/lib --add-modules javafx.controls,javafx.fxml ACReno.jar
```
