/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author Ali Otaku
 */
public class Employee extends Person {
     private int ID;
     private final int WORK_HOURS=10;
     private String entryTime;
     private String exitTime;
     private double salary;
     private String task;
     private int T_ID;
     protected String  penalites;
    public void setID(int ID) {
        this.ID = ID;
    }

     
    public void setTask(String task) {
        this.task = task;
    }
     

    public String getTask() {
        return task;
    }
     
     
    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

   
    
    
    public String getEntryTime() {
        return entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }
    

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getID() {
        return ID;
     }
   
     public  boolean  chackFinishTasks(TeamLeader t)
     {
         if (t.getProgress()==100)
             return true ;
         else 
             return false ;
                     
     }
      
      public  void requestVacation(Vacation v ,String v_StartDate,String v_EndDate, String v_Reason)
      {
         v.setStartDate(v_StartDate);
         v.setEndDate(v_EndDate);
         v.setReason(v_Reason);
      }

    public String getPenalites() {
        return penalites;
    }

    public int getT_ID() {
        return T_ID;
    }

    public void setT_ID(int T_ID) {
        this.T_ID = T_ID;
    }
     
      
    
}

