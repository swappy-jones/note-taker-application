package com.example.roomsample;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class AllNotesActivity extends AppCompatActivity implements View.OnClickListener, NotesRecyclerAdapter.INoteClick, NotesRecyclerAdapter.IItemCheck {
    private NoteViewModel noteViewModel;
    private RecyclerView recyclerAllNotes;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private FloatingActionButton floatingActionButton;
    private View app_bar = null;
    private SearchAnimationToolbar searchAnimationToolbar;
    private Toolbar searchToolbar;
    private EditText edtSearch;
    private TextView txtHeader;
    private Menu currentMenu;
    private ImageView imgNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manageToolbar();
        recyclerAllNotes = findViewById(R.id.recyclerAllNotes);
        floatingActionButton = findViewById(R.id.floatingActionAdd);
        imgNoResult = findViewById(R.id.imgNoResult);
        floatingActionButton.setOnClickListener(this);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        notesRecyclerAdapter = new NotesRecyclerAdapter(new ArrayList<NoteEntity>());
        notesRecyclerAdapter.setiNoteClick(this);
        notesRecyclerAdapter.setiItemCheck(this);
        noteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                if (noteEntities.size()==0){
                    imgNoResult.setVisibility(View.VISIBLE);
                    recyclerAllNotes.setVisibility(GONE);
                    return;
                }
                imgNoResult.setVisibility(GONE);
                recyclerAllNotes.setVisibility(View.VISIBLE);
                notesRecyclerAdapter.setNoteEntityArrayList(new ArrayList<>(noteEntities));
                recyclerAllNotes.setLayoutManager(new LinearLayoutManager(AllNotesActivity.this));
                recyclerAllNotes.setAdapter(notesRecyclerAdapter);
            }
        });
    }

    private void manageToolbar() {
        if (app_bar==null){
            app_bar = findViewById(R.id.header);
            Toolbar toolbar = app_bar.findViewById(R.id.includeToolbar);
            setSupportActionBar(toolbar);
            ActionBar supportActionBar = getSupportActionBar();
            supportActionBar.setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
            txtHeader = toolbar.findViewById(R.id.txtHeader);
            edtSearch = toolbar.findViewById(R.id.edtSearch);
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    notesRecyclerAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            txtHeader.setText("Notes");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,AddOrEditNoteActivity.class);
        intent.putExtra(ExtraUtil.ADD_NOTE,true);
        startActivity(intent);
    }

    @Override
    public void noteClickListener(NoteEntity noteEntity) {
        Intent intent = new Intent(this,AddOrEditNoteActivity.class);
        intent.putExtra(ExtraUtil.EDIT_NOTE,noteEntity);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        currentMenu = menu;
        menu.findItem(R.id.action_alarm).setVisible(false);
        menu.findItem(R.id.action_pin).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_cancel_alarm).setVisible(false);
        menu.findItem(R.id.action_cancel).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                edtSearch.setVisibility(View.VISIBLE);
                txtHeader.setVisibility(GONE);
                edtSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                currentMenu.findItem(R.id.action_search).setVisible(false);
                currentMenu.findItem(R.id.action_cancel).setVisible(true);
                return true;
            case R.id.action_cancel:
                edtSearch.setText("");
                return true;
            case android.R.id.home:
                edtSearch.setVisibility(GONE);
                txtHeader.setVisibility(View.VISIBLE);
                edtSearch.clearFocus();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(),0);
                currentMenu.findItem(R.id.action_search).setVisible(true);
                currentMenu.findItem(R.id.action_cancel).setVisible(false);
                return true;
        }
        return true;
    }

    @Override
    public void hasItems(Boolean result) {
        if (result){
            imgNoResult.setVisibility(GONE);
            recyclerAllNotes.setVisibility(View.VISIBLE);
        } else{
            imgNoResult.setVisibility(View.VISIBLE);
            recyclerAllNotes.setVisibility(GONE);
        }
    }
}
