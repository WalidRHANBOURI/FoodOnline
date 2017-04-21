/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Device;
import bean.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author BOUKAID
 */
@Stateless
public class DeviceFacade extends AbstractFacade<Device> {

    @PersistenceContext(unitName = "com.mycompany_FoodOnline_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DeviceFacade() {
        super(Device.class);
    }
    public List<Device> findDeviceByUser(User user){
      if(user==null || user.getLogin()==null){
          return new ArrayList<>();
      }  
      else {
          String requette="select d from Device d where d.user.login='"+user.getLogin()+"'";
          return em.createQuery(requette).getResultList();
      }
    }
    public void save(User user ,Device device){
        device.setUser(user);
        create(device);
    }
    public int enregistrerDevice(User user,Device device){
        int i = checkDevice(user, device);
        if(i==1 || i==-1){
            device.setUser(user);
            create(device);
            return 1;
        }
        else{
            return -1;
        }
    }
    public int checkDevice(User user,Device device ){
        List<Device> list=findDeviceByUser(user);
        if(list.isEmpty()){
            return 1;
        } else{
            for (Device device1 : list) {
                if(device1.getBrowser().equalsIgnoreCase(device.getBrowser())
                        && device1.getDeviceCategorie().equalsIgnoreCase(device.getDeviceCategorie())
                        && device1.getOperatingSystem().equalsIgnoreCase(device.getOperatingSystem())){
                    return 2;
                }
            }
        }
        return -1;
    }
}
