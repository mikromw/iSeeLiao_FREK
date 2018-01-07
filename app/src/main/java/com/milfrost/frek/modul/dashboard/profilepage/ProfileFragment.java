package com.milfrost.frek.modul.dashboard.profilepage;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.milfrost.frek.R;
import com.milfrost.frek.modul.dashboard.profilepage.ChangePasswordPage.ChangePasswordActivity;
import com.milfrost.frek.modul.dashboard.profilepage.EditProfilePage.EditProfileActivity;
import com.milfrost.frek.modul.loginPage.LoginPage;
import com.milfrost.frek.utils.ApiRequest;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ProfileFragmentInterface.View{

    View view;

    TextView nameText;
    ImageView cover;
    ImageView userProfile;

    LinearLayout editProfileCont;
    LinearLayout changePwdCont;
    LinearLayout reportCont;
    LinearLayout privacyPolicyCont;
    LinearLayout logOutCont;

    ProfileFragmentPresenter profileFragmentPresenter;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews();
        initObjects();
        setEvents();

        return view;
    }

    private void initViews(){
        editProfileCont = (LinearLayout)view.findViewById(R.id.edit_profile_cont);
        changePwdCont = (LinearLayout)view.findViewById(R.id.change_pwd_cont);
        reportCont = (LinearLayout)view.findViewById(R.id.report_problem_cont);
        privacyPolicyCont = (LinearLayout)view.findViewById(R.id.priv_policy_cont);
        logOutCont = (LinearLayout)view.findViewById(R.id.log_out_cont);
        nameText = (TextView)view.findViewById(R.id.profile_name);
        cover = (ImageView) view.findViewById(R.id.cover_img);
        userProfile = (ImageView)view.findViewById(R.id.user_profile);
    }

    private void initObjects(){
        profileFragmentPresenter = new ProfileFragmentPresenter(getContext());
        profileFragmentPresenter.viewInterface=this;
        profileFragmentPresenter.loadData();
    }

    private void setEvents(){
        editProfileCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfile();
            }
        });
        changePwdCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePwd();
            }
        });
        reportCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReportProblem();
            }
        });
        privacyPolicyCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrivacyPolicy();
            }
        });
        logOutCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void openPrivacyPolicy(){

    }

    private void openReportProblem(){

    }

    private void logOut(){
        ApiRequest.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginPage.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    private void openEditProfile(){
        Intent intent = new Intent(getContext(),EditProfileActivity.class);
        getContext().startActivity(intent);
    }

    private void openChangePwd(){
        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
        getContext().startActivity(intent);
    }



    @Override
    public void updateName(String name) {
        nameText.setText(name);
    }

    @Override
    public void updateUserProfile(String url) {
        Picasso.with(getContext())
                .load(url)
                .into(userProfile);
    }

    @Override
    public void updateCover(String url) {
        Picasso.with(getContext())
                .load(url)
                .into(cover);
    }
}
