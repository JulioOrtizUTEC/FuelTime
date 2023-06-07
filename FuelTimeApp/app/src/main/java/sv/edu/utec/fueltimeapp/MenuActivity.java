package sv.edu.utec.fueltimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.zip.Inflater;

public class MenuActivity extends MainActivity {

    DrawerLayout drwLayout;
    Toolbar tlBarra;
    NavigationView navView;
    Button btnIniciarSesion, btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        try {
            Thread.sleep(2000);
            setTheme(R.style.Theme_FuelTimeApp);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            fragmentosR(new InicioFragment());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        tlBarra = findViewById(R.id.toolbar);
        drwLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navigator);
        btnIniciarSesion=findViewById(R.id.btnInicioSesion);
        btnSalir = findViewById(R.id.btnSalir);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drwLayout,tlBarra,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drwLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white,null));

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_home:
                        drwLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(),"Inicio",Toast.LENGTH_SHORT).show();
                        fragmentosR(new InicioFragment());
                        break;
                    case R.id.nav_agrega_horario:
                        drwLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(),"CRUD Horarios",Toast.LENGTH_SHORT).show();
                        fragmentosR(new CrudHorariosFragment());
                        break;
                    case R.id.nav_horario:
                        drwLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(),"Horarios",Toast.LENGTH_SHORT).show();
                        fragmentosR(new HorariosFragment());
                        break;
                    case R.id.nav_gasolinera:
                        drwLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(),"Gasolineras",Toast.LENGTH_SHORT).show();
                        fragmentosR(new GasolineraFragment());
                        break;
                    case R.id.nav_leal:
                        drwLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(),"Puntos Leal", Toast.LENGTH_SHORT).show();
                        fragmentosR(new Puntos_LealFragment());
                        break;
                    case R.id.nav_perfil:
                        drwLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(),"Perfil",Toast.LENGTH_SHORT).show();
                        fragmentosR(new PerfilFragment());
                        break;
                }

                return true;
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drwLayout.closeDrawer(GravityCompat.START);
                fragmentosR(new InicioSesionFragment());
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @SuppressLint("CommitTransaction")
    private void fragmentosR(Fragment fragment){
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmLayoutContainer,fragment);
        fragmentTransaction.commit();
    }

}