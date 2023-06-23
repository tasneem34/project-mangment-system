/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author Ali Otaku
 */
public class DatabaseConnection {

    public DatabaseConnection() {
            try {
                try {
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null, "Error");
                }
                connect = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-1VMRK9D\\SQLEXPRESS:1433;databaseName=Project","user","pl2project");
                st = connect.createStatement();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE,null, ex);
        }
    }
     Connection connect;
     Statement st;
     int id;
    public ResultSet viewUsers()
     {
         Person user = new Person();
         ResultSet rs;
        try {
            rs = st.executeQuery("Select Username.id,user_name,password, type as role from Username inner join UserTypes  on Username.role_id = UserTypes.id");
               return rs;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
return null;
     }
    public ResultSet veiwTeamLeaders() /// TO BE CONTINED>>>
    {
        ResultSet rs = null;
         
        try {
           return  rs= st.executeQuery("Select user_name from Username where role_id=2");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
       return rs;
        
    }
     public void insertUser(Person user)
     {
         try{
          ResultSet rs =  st.executeQuery("Select id from Username where user_name ='"+user.getUserName()+"'"); //?????
          if(!rs.next())
          {
           rs =  st.executeQuery("Select id from UserTypes where type='"+user.getRole()+"'");
           while(rs.next()){
            id = rs.getInt("id");
          }
          if (id==0)
          {
             JOptionPane.showMessageDialog(null, "Invalid Role ","Error",JOptionPane.ERROR_MESSAGE); 
 
          }
                 else {
                     st.executeUpdate("Insert into Username Values('" + user.getUserName() + "','" + user.getPass() + "','" + id + "')");
                     if (id == 1) {
                         retriveEmployeeId(user);
                         st.executeUpdate("Insert into Employee (e_id) Values('"+user.getId()+"')");

                     }
                     JOptionPane.showMessageDialog(null, "User Has Been Added Successfully", "Notification", JOptionPane.INFORMATION_MESSAGE);

                 }
             }
          else JOptionPane.showMessageDialog(null, "This username already exists in the database"); 
        
          
         }
         catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        
        }
       
     }
      private void retriveEmployeeId(Person user )
      {
         try { 
            ResultSet rs =st.executeQuery("Select * from Username where user_name='" + user.getUserName() + "'");

          if(rs.next()){
          int id = rs.getInt("id");
           user.setId(id);  
      }
      }
     catch (SQLException ex) {
     Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        
        }
         
      }
     
     public void deleteUser(int x)
     {
         try {
         id =x;
          st.executeUpdate("Delete From Username Where id='"+id+"'");
          
           JOptionPane.showMessageDialog(null, "Deleted Successfully ","Notification",JOptionPane.INFORMATION_MESSAGE); 

     }
         catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }
     //
    private boolean checkUser(Person user)
    {
        try {
      ResultSet rs = st.executeQuery("Select * from Username where user_name='"+user.getUserName()+"'");
      if(rs.next()){
          if(rs.getInt("id")==user.getId())
              return false;
          return true;
      }
      else
          return false;
    }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
         return false;
    }
     public void updateUser(Person user)
     {
         try{
         DatabaseConnection db = new DatabaseConnection();
          if(db.checkUser(user)) {
        JOptionPane.showMessageDialog(null,"This user already exists in the database","Error",JOptionPane.ERROR_MESSAGE); return; }
       ResultSet rs =  st.executeQuery("Select id from UserTypes where type='"+user.getRole()+"'"); //CHECK ME
    
       if(rs.next()){
         id = rs.getInt("id");
       st.executeUpdate("Update Username set user_name='"+user.getUserName()+"',password='"+user.getPass() +"',role_id='"+id+"' where id='"+user.getId()+"'"); // CHECK ME
      
       st.executeUpdate("Delete from Employee where e_id='"+user.getId()+"'");
        
         if (id==1)
         {
          
           st.executeUpdate("Insert into Employee (e_id) Values('"+user.getId()+"')");
         }
       
        JOptionPane.showMessageDialog(null,"Updated Successfully ","Notification",JOptionPane.INFORMATION_MESSAGE);
       
       }
       else JOptionPane.showMessageDialog(null,"Invalid Role","Error",JOptionPane.ERROR_MESSAGE);
       
     }
          catch(Exception ex)
       {
           Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);

       }
        
     }
     
   


     public ResultSet viewAllProjects()
     {
         try {
        return st.executeQuery("Select * from Projects ");
     }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
     }
     public int checkLogin(Person user)
     {
        try {
         ResultSet rs = st.executeQuery("Select * from Username Where user_name='"+user.getUserName()+"' And password ='"+user.getPass()+"'");
         while(rs.next())
         {
             id = rs.getInt("role_id");
             user.setId(rs.getInt("id"));
         }
         switch(id)
         {
             case 1: return 1;
             case 2: return 2; 
             case 3: return 3; 
             case 4: return 4; 
             default: return 0;
         }
     }
         catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
     }
     public void viewProjectProgress()
     {
        try { 
         float percentage;
         String project_name;
          ResultSet rs = st.executeQuery("Select project_name,progress from Projects");
         while (rs.next()) {
           percentage = rs.getFloat("progress");  
               project_name = rs.getString("project_name");
               System.out.println(percentage+" "+project_name);
       
             // Check me
         }
             
     }
         catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
      public ResultSet viewReport()
     {
         try {
        ResultSet rs = st.executeQuery("Select u.user_name,u.id,t.name,e.entry_time,e.exit_time,e.date,t.progress,YEAR(e.date) year,MONTH(e.date) month from Username u,Task t,EmployeeAttendance e where u.id=t.e_id And t.e_id=e.e_id");
             /*username - id - task - task progress - entry time - exit time */
          
                return rs;
                   
     }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
     }
     public ResultSet viewCompletedTask(int tid)
     {
         try {
      ResultSet rs = st.executeQuery("Select name as task_name,start_date ,end_date,project_name, username.user_name from Task t inner join Projects on Projects.id = t.p_id  inner join Username on t.e_id = Username.id where team_leader_id = '"+tid+"' And t.progress>=100 ");
    return rs;    
     }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
     }
     public void assignTask(Projects project , Tasks task) //incomplete
     { 
          Date start_date = new Date(task.getStartYear(),task.getStartMonth(),task.getStartDay());
          Date end_date = new Date(task.getEndYear(),task.getEndMonth(),task.getEndDay());

          if(end_date.before(start_date)) {
              JOptionPane.showMessageDialog(null,"Invalid Date", "Error",JOptionPane.ERROR_MESSAGE);
             return;
          }
          try  {
       ResultSet rs = st.executeQuery("Select id from Task where name ='"+task.getName()+"'");
         if (!(rs.next())) {
         
          rs = st.executeQuery("Select id from Projects where  project_name ='"+project.getName()+"'");
         if(rs.next()) {
             id=rs.getInt("id");
             st.executeUpdate ("insert into Task (name ,start_date,end_date ,team_leader_id,e_id,p_id) Values ('"+task.getName()+"','"+task.getStartDate()+"','"+task.getEndDate()+"','"+task.getT_ID()+"','"+task.getE_ID()+"','"+id+"')");
              JOptionPane.showMessageDialog(null, "Assigned Successfully","Notification",JOptionPane.INFORMATION_MESSAGE);

         } else {
             JOptionPane.showMessageDialog(null, "Invalid project name","Error",JOptionPane.ERROR_MESSAGE);
         }
     }
         else 
         {
            JOptionPane.showMessageDialog(null," This Task is already exist ","Error",JOptionPane.ERROR_MESSAGE);
         }
     }
           catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     
     
     public void  setPunishment(int id,String p)
     {
         if (p!=null){
         try {
      
    st.executeUpdate("insert into Penalities values('"+id+"','"+p+"')"); //incomplete,it's a gui dependent method. note: database may need to go through some modification if you are willing to abandon some compilcated *features*
     }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
         }
         
     }
   public  ResultSet viewVacationRequest(int id)
     {
      try {
         ResultSet rs = st.executeQuery("Select v.e_id,un.user_name, v.start_date, v.end_date, v.reason from Username un,Vacation v, Employee e where v.state=1 and v.e_id=un.id and e.team_leader_id ='"+id+"' and e.e_id = un.id");
         
         return rs;
     }
       catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
      return null;
     }
     public void setVacation(int state, int e_id)
     {
         try {
         st.executeUpdate("update Vacation  set state='"+state+"' Where e_id = '"+e_id+"' and state=1");
     }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
   public  void setPenalities(int e_id,int type)
     {
         try {
         st.executeUpdate("insert into Penalities values('"+e_id+"','"+type+"')");
     }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     
     public void assignEntry(int id) //
     {
         GregorianCalendar date =new GregorianCalendar();
         String time = date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE)+":"+date.get(Calendar.SECOND);
         String d = ""+(date.get(Calendar.YEAR))+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DAY_OF_MONTH);
         try {
         ResultSet rs = st.executeQuery("Select * from EmployeeAttendance where e_id='"+id+"' And date='"+d+"'");
         if(!rs.next())
         {
             st.executeUpdate("insert into EmployeeAttendance(entry_time,date,e_id) values('"+time+"','"+d+"','"+id+"') ");
             JOptionPane.showMessageDialog(null, "daily attendence has been recorded successfully ","Notification",JOptionPane.INFORMATION_MESSAGE);
         }
         }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     
     public void assignExit(int id)
     {
         GregorianCalendar date =new GregorianCalendar();
         String time = date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE)+":"+date.get(Calendar.SECOND);
         String d = ""+(date.get(Calendar.YEAR))+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DAY_OF_MONTH);
        
           try { 
       //  st.executeUpdate("Update EmployeeAttendance set exit_time ='"+time+"' where e_id='"+id+"' And date='"+d+"' ");
       st.executeUpdate("Update EmployeeAttendance set exit_time =cast('"+time+"' as time) where e_id='"+id+"' And date=cast('"+d+"' as date)");
                 JOptionPane.showMessageDialog(null,"Exit time has been stored successfully","Notification",JOptionPane.INFORMATION_MESSAGE);
         }
    catch(Exception e)
         {
             JOptionPane.showMessageDialog(null,"Couldn't store exit time","Error",JOptionPane.ERROR_MESSAGE);
         } 
     }
     public String monthlyWorkHours(int id,int year, int month)
     {
         try {
          ResultSet rs = st.executeQuery("Select DATEDIFF(millisecond,entry_time,exit_time) as w from EmployeeAttendance where e_id='"+id+"' and month(date)='"+month+"' and year(date)='"+year+"'");
         long time =0;
         while(rs.next())
         {

             time = time+ rs.getInt("w");
             
             
         }
         String work_hours=  (time/1000/60/60%24)+ "H :"+ (time/1000/60%60) +"M" ;
         return work_hours;
        
     }
          catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
         
     }
     public int  requestVacation(Vacation request)
     {
          Date start_date = new Date(request.getStartYear(),request.getStartMonth(),request.getStartDay());
          Date end_date = new Date(request.getEndYear(),request.getEndMonth(),request.getEndDay());

        
          if(end_date.before(start_date)) {
             
             return 3;
          }
         id = request.getEmployeeID();
         try {
         ResultSet rs = st.executeQuery("Select * from Vacation where e_id='"+id+"' and state=1");
         if(!rs.next())
         {st.executeUpdate("Insert into Vacation(e_id,start_date,end_date,reason) Values('"+id+"','"+request.getStartDate()+"','"+request.getEndDate()+"','"+request.getReason()+"')"); return 1;}
         else return 2;
         }   catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
     }
         
     public ResultSet viewPenalities(int id)
     {
         try {
          ResultSet rs = st.executeQuery("Select type from Penalities where e_id='"+id+"'");
   return rs;
     } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
         
     }
   public  ResultSet ViewTask(int id){
       try {
       ResultSet rs = st.executeQuery("Select * from Task inner join Projects on p_id=Projects.id where e_id='"+id+"'");
       return rs;
     } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
   }
   public  int findId(Person user)
     {
         try {
         ResultSet rs = st.executeQuery("Select * from Username where user_name='"+user.getUserName()+"' and password='"+user.getPass()+"'");
         rs.next();
         return rs.getInt("id");
     } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
     }
   public ResultSet viewTeamEmployee(int id)
   {
       try {
       ResultSet  rs = st.executeQuery("Select * from Username u, Employee e where e.e_id = u.id and team_leader_id='"+id+"'");
      return rs;
   }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
   }
   public String lastVacationRequestState(int id)
   {
       try {
       ResultSet rs = st.executeQuery("Select top 1 vt.type from Vacation v inner join VacationType vt on v.state = vt.id where v.e_id='"+id+"' order by v.id DESC");
       rs.next();
       return (String)rs.getString("type");
   }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
   }
   public ResultSet CalculateProgress()
   {
       try {
  ResultSet rs = st.executeQuery("Select sum(progress)/count(progress)as project_progress,p_id  from Task  group by p_id ");
 return rs;

} catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
   }

    public void updateProgress(ResultSet rs) {
        try {
        while (rs.next()) {
            float progress = rs.getFloat("project_progress");
            id = rs.getInt("p_id");
            st.executeUpdate("Update Projects set progress='" + progress + "' where id='" + id + "'");
        }
    } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 public  void addProject(Projects pro){
     
         Date start_date = new Date(pro.getStartYear(),pro.getStartMonth(),pro.getStartDay());
         Date end_date = new Date(pro.getEndYear(),pro.getEndMonth(),pro.getEndDay());
             if(end_date.before(start_date)) {
              JOptionPane.showMessageDialog(null,"Invalid Date", "Error",JOptionPane.ERROR_MESSAGE);
             return;
          }
            id=pro.getID();
            try {
              ResultSet rs = st.executeQuery("Select id from Projects where project_name ='"+pro.getName()+"'");
              if (!(rs.next()))
              {       
            st.executeUpdate("Insert into Projects (project_name,entry_time,exit_time) values ('"+pro.getName()+"','"+pro.getStartDate()+"','"+pro.getEndDate()+"')");
              
              JOptionPane.showMessageDialog(null," Project Added successfully","Notification",JOptionPane.INFORMATION_MESSAGE);
              }
              else {
                  JOptionPane.showMessageDialog(null," Project is already exist ","Error",JOptionPane.ERROR_MESSAGE);
            }
 
        }
             catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Addition of the project has failed ","Error",JOptionPane.ERROR_MESSAGE);
        }
 }
 
  public void setProgress( Tasks task  )
  {
      try {
     
      id=task.getE_ID();
        int rs =st.executeUpdate(" update Task set progress ='"+task.getProgress()+"'" +" where e_id = '"+id+"' and name ='"+task.getName()+"'" );
     JOptionPane.showMessageDialog(null," Progress has been add successfully","Notification",JOptionPane.INFORMATION_MESSAGE);
 
  } catch (Exception ex) {
      JOptionPane.showMessageDialog(null," Progress Couldn't be added  ","Error",JOptionPane.ERROR_MESSAGE);
        }
  }
  public void updateEmployeeStatus(Employee employee)
  {
   try {
     st.executeUpdate("update Employee set salary='"+employee.getSalary()+"',team_leader_id='"+employee.getT_ID()+"' where e_id='"+employee.getID()+"'");
        JOptionPane.showMessageDialog(null,"Updated Successfully","Notifcation",JOptionPane.INFORMATION_MESSAGE);
     
  } catch (Exception ex) {
      //JOptionPane.showMessageDialog(null," An Error Occurred in Updating Employee Status ","Error",JOptionPane.ERROR_MESSAGE);
      
      Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);

        }
  }
 public int getTeamLeaderId(String name)
 {
     try {
    ResultSet rs = st.executeQuery("Select id from Username where user_name='"+name+"'");
    rs.next();
    return rs.getInt("id");
 } catch (SQLException ex) {
     JOptionPane.showMessageDialog(null," Couldn't list TeamLeaders  ","Error",JOptionPane.ERROR_MESSAGE);
        }
        return -1;
 }
 }
 
 

    

