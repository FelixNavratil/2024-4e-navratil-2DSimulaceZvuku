module com.example.rp3_2d_simulace_zvuku_v_mistnosti {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.rp3_2d_simulace_zvuku_v_mistnosti to javafx.fxml;
    exports com.example.rp3_2d_simulace_zvuku_v_mistnosti;
}