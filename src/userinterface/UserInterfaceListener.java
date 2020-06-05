package userinterface;

import mapvisiblecontent.Fragment;

public interface UserInterfaceListener{
public void exitProgram();
public void goToFragment(Fragment inFragment);
public Fragment getCurrentLeftUpFragment();

}