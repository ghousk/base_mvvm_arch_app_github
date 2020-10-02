package com.innovativequest.base_mvvm_arch_app_github.util;


import com.innovativequest.base_mvvm_arch_app_github.BuildConfig;

/**
 * Created by Ghous on 2020-10-02.
 */
public class EnvironmentParameters {
    private String mBaseUrl;
    private String mCurrentEnv;

    public EnvironmentParameters(){
        if(BuildConfig.FLAVOR == "baseDev"){
           setBaseDevEnvironment();
        }
        else if (BuildConfig.FLAVOR == "baseUat") {
           setBaseUatEnvironment();
        }
        else if (BuildConfig.FLAVOR == "baseLive") {
            setBaseLiveEnvironment();
        }
        else{
            setBaseDevEnvironment();
        }
    }

    public void setBaseDevEnvironment(){
        mCurrentEnv = "BaseDev";
        mBaseUrl        = AppConstants.BASE_URL_DEV;
    }

    public void setBaseUatEnvironment(){
        mCurrentEnv = "BaseUat";
    }

    public void setBaseLiveEnvironment(){
        mCurrentEnv = "BaseLive";

    }

    public String getBaseURL(){ return mBaseUrl; }

    //Sets
    // Add any Late Initialised Sets here

}
