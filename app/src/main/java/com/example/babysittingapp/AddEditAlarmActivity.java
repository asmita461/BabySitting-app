package com.example.babysittingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.babysittingapp.R;
import com.example.babysittingapp.DatabaseHelper;
import com.example.babysittingapp.Alarm;
import com.example.babysittingapp.LoadAlarmsService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class AddEditAlarmActivity extends AppCompatActivity {

    public static final String ALARM_EXTRA = "alarm_extra";
    public static final String MODE_EXTRA = "mode_extra";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EDIT_ALARM,ADD_ALARM,UNKNOWN})
    @interface Mode{}
    public static final int EDIT_ALARM = 1;
    public static final int ADD_ALARM = 2;
    public static final int UNKNOWN = 0;

    private boolean mAlarmChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getToolbarTitle());

        final Alarm alarm = getAlarm();

        if(getSupportFragmentManager().findFragmentById(R.id.edit_alarm_frag_container) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.edit_alarm_frag_container, AddEditAlarmFragment.newInstance(alarm))
                    .commit();
        }


        if(getSupportFragmentManager().findFragmentById(R.id.edit_alarm_frag_container) != null) {
            AddEditAlarmFragment fragment = (AddEditAlarmFragment) getSupportFragmentManager().findFragmentById(R.id.edit_alarm_frag_container);
            mAlarmChanged = fragment.hasAlarmChanged();
        }

    }

    private Alarm getAlarm() {
        switch (getMode()) {
            case EDIT_ALARM:
                return getIntent().getParcelableExtra(ALARM_EXTRA);
            case ADD_ALARM:
                final long id = DatabaseHelper.getInstance(this).addAlarm();
                LoadAlarmsService.launchLoadAlarmsService(this);
                return new Alarm(id);
            case UNKNOWN:
            default:
                throw new IllegalStateException("Mode supplied as intent extra for " +
                        AddEditAlarmActivity.class.getSimpleName() + " must match value in " +
                        Mode.class.getSimpleName());
        }
    }

    private @Mode int getMode() {
        final @Mode int mode = getIntent().getIntExtra(MODE_EXTRA, UNKNOWN);
        return mode;
    }

    private String getToolbarTitle() {
        int titleResId;
        switch (getMode()) {
            case EDIT_ALARM:
                titleResId = R.string.edit_alarm;
                break;
            case ADD_ALARM:
                titleResId = R.string.add_alarm;
                break;
            case UNKNOWN:
            default:
                throw new IllegalStateException("Mode supplied as intent extra for " +
                        AddEditAlarmActivity.class.getSimpleName() + " must match value in " +
                        Mode.class.getSimpleName());
        }
        return getString(titleResId);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /**if(!mAlarmChanged){
                 NavUtils.navigateUpFromSameTask(AddEditAlarmActivity.this);
                 return true;
                 }**/
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AddEditAlarmActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent buildAddEditAlarmActivityIntent(Context context, @Mode int mode) {
        final Intent i = new Intent(context, AddEditAlarmActivity.class);
        i.putExtra(MODE_EXTRA, mode);
        return i;
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    public void onBackPressed(){
        /**if(!mAlarmChanged){
         super.onBackPressed();
         return;
         }**/

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }


}