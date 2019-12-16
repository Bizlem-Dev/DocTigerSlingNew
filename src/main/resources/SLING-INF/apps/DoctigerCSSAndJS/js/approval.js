
if(document.getElementById("email").value=="anonymous"){
	var Email= "viki@gmail.com";
}else{
	var Email= document.getElementById("email").value;
}
var group=document.getElementById("working-group-Dropdown-id").value;

alert("approvaljs group"+group)
$( document ).ready(function() {
	displaytaskslist();
});


//approvetaskid
//rejecttaskid


  
$("body").on("click", "#approvetaskid", function(){
	
	console.log("click approve");
	var tdobj =$(this).parent("td");
	var jsonobj={};
	var TemplateName= tdobj.attr('TemplateName');
   var Type =tdobj.attr('Type');
  var creator= tdobj.attr('creator');
  var approverSFemailIde=tdobj.attr('approverSFemailIde') ;
   var approvSFusername= tdobj.attr('approvSFusername');
   var taskid= tdobj.attr('task-id');
   
   jsonobj["TemplateName"]=TemplateName;
   jsonobj["group"]=group;

	jsonobj["Type"]=Type;
	jsonobj["creator"]=creator;
	jsonobj["approverSFemailId"]=approverSFemailIde;
	jsonobj["approvSFusername"]=approvSFusername;
	jsonobj["task-id"]=taskid;
  	console.log("input json"+JSON.stringify(jsonobj));
	     	
 	
 		 $.ajax({
 		  type: 'POST',
 		  url: '/portal/servlet/service/approvetask',
 		  async:false,
		  data:JSON.stringify(jsonobj),
		  dataType: 'json', 
		   contentType: 'application/json',
 		  success: function (dataa) {
 		  // alert(JSON.stringify(dataa));
 		  console.log("in approve success");
		  console.log("dataa "+dataa);

		  console.log(JSON.stringify(dataa));
 		  }
 		 
 		 });
});

//{"email":"doctiger@xyz.com", "TemplateName":"temp2","Type":"Template","creator":"doctiger@xyz.com", "approverSFemailId":"doctiger@xyz.com", "approvSFusername" : doctiger@xyz.com,"task-id":"336" }


$("body").on("click", "#rejecttaskid", function(){
	console.log("click reject");

	var tdobj =$(this).parent("td");
	var jsonobj={};
	var TemplateName= tdobj.attr('TemplateName');
   var Type =tdobj.attr('Type');
  var creator= tdobj.attr('creator');
  var approverSFemailIde=tdobj.attr('approverSFemailIde') ;
   var approvSFusername= tdobj.attr('approvSFusername');
   var taskid= tdobj.attr('task-id');
   jsonobj["email"]=Email;
   jsonobj["group"]=group;

   jsonobj["TemplateName"]=TemplateName;
	jsonobj["Type"]=Type;
	jsonobj["creator"]=creator;
	jsonobj["approverSFemailId"]=approverSFemailIde;
	jsonobj["approvSFusername"]=approvSFusername;
	jsonobj["task-id"]=taskid;
 	console.log("input json"+JSON.stringify(jsonobj));
	     	
		 $.ajax({
		  type: 'POST',
		  url: '/portal/servlet/service/CheckApproval',
		  async:false,
		  data:JSON.stringify(jsonobj),
		  dataType: 'json', 
		   contentType: 'application/json',
		  success: function (dataa) {
			  console.log("in reject success");
			  console.log(dataa);
		  // alert(JSON.stringify(dataa));
		  }
		 });
});


  function displaytaskslist(){
  	alert("in display doc table")
  	//{"approverSFemailId":"doctiger@xyz.com","approvSFusername":"doctiger@xyz.com","username":"doctiger@xyz.com","password":"12345"}
  	var jsonobj={};
  	//jsonobj["approverSFemailId"]="doctiger@gmail.com";
  	jsonobj["approverSFemailId"]="Email";
  	jsonobj["approvSFusername"]=Email;
  	jsonobj["username"]=Email;
  	jsonobj["password"]="12345";
  	console.log("input json"+JSON.stringify(jsonobj));
  	
  	console.log("in display doc table")
  		 $.ajax({
  		  type: 'POST',
  		  url: '/portal/servlet/service/workflowinfo',
  		  async:false,
		  data:JSON.stringify(jsonobj),
		  dataType: 'json', 
		   contentType: 'application/json',
  		  success: function (dataa) {
  		   alert(JSON.stringify(dataa));
  	 //  var json = JSON.parse(dataa);
  		//   console.log(json);    
  		   var tab =document.getElementById("pendingtasktableid");
  		   var oTBody = tab.getElementsByTagName("tbody")[0];
  		       oTBody.innerHTML = "";   
  		   
   var table = document.getElementById("pendingtasktableid");
   var tablebody = table.getElementsByTagName("tbody")[0];

   
//   {
//	   "TemplateName":"Temp03",
//	   "Type":"Template",
//	   "Description":"hjuoiuoiuoi",
//	   "AttachmentLink":"http://35.221.183.246:8082/TemplateLibraryAdvanced/Temp03.docx",
//	   "creator":"doctiger@xyz.com",
//	   "task-id":"53"
//	   },
//	   {
//	   "TemplateName":"Clause012",
//	   "Type":"Clauses",
//	   "AttachmentLink":"{\"Email\":\"doctiger@xyz.com\",\"clauseName\":\"Clause012\",\"ClauseId\":\"0\",\"SFEmail\":\"doctiger@xyz.com\"}",
//	   "creator":"doctiger@xyz.com",
//	   "task-id":"48"
//	   }
      
   if(dataa.length>0){
  for (var i = 0; i < dataa.length; i++) {
	  console.log("dataa.length  "+dataa.length);
	  
	   var  TemplateName = dataa[i].TemplateName;
	   console.log(TemplateName);

 	var count =i;
      var Type = dataa[i].Type;
      var creator=dataa[i].creator;
      var taskid = dataa[i]['task-id'];
      
      //{"TemplateName":"temp2","Type":"Template","creator":"doctiger8@gmail.com", "approverSFemailId":"doctiger8@xyz.com", "approvSFusername" : doctiger@xyz.com, "task-id":"336" }  
      
       var links='<td style="border-color:#fff;"  ><a href="#" id="approvetaskid"><i class="fa fa-check approver-icon" aria-hidden="true" style="color:green;"></i></a><a href="#" id="rejecttaskid" ><i class="fa fa-times approver-icon" aria-hidden="true" style="color:red;"></i></a></td>';
      var row = tablebody.insertRow(i);
    //  table.rows[i + 1].className = "parent-tr";
      var cell1 = row.insertCell(0);
      var cell2 = row.insertCell(1);
      var cell3 = row.insertCell(2);
      var cell4 = row.insertCell(3);
      var cell5 = row.insertCell(4);

      cell1.innerHTML = TemplateName ;
      cell2.innerHTML = Type;
      if(Type == "Clauses"){
    	  
    //{" email":"doctiger@xyz.com","clausename":"clause01","clauseid":"0","sfemail":"doctiger@gmail.com"} 
    	  
          var AttachmentLink1 = dataa[i].AttachmentLink; 
          var attach= JSON.parse(AttachmentLink1);
          console.log("attach  "+JSON.stringify(attach));
          var aEmail=attach.email
          var aSFEmail=attach.sfemail
          var aclausename=attach.clausename
          var aclauseid=attach.clauseid
          
          cell4.innerHTML = '<a href="" id="viewclause" Email="'+aEmail+'" SFEmail="'+aSFEmail+'" ClauseName="'+aclausename+'" Clauseid="'+aclauseid+'">view clause </a>';
      }else{
          var AttachmentLink = dataa[i].AttachmentLink;
          cell4.innerHTML = '<a href="'+AttachmentLink+'">Download file here</a>';
          var Description = dataa[i].Description;
          cell3.innerHTML = Description;
      }
      
      cell5.innerHTML = links;
      cell5.setAttribute("TemplateName",TemplateName);
      cell5.setAttribute("Type", Type);
      cell5.setAttribute("creator", creator);
      cell5.setAttribute("approverSFemailIde",Email );
      cell5.setAttribute("approvSFusername",Email);
      cell5.setAttribute("task-id", taskid);
     }
  		  }	   
  		  } 
  		  });
  	
  }