
///var Email= "viki@gmail.com";
if(document.getElementById("email").value=="anonymous"){
	var Email= "viki@gmail.com";
}else{
	var Email= document.getElementById("email").value;
}


var group=" ";
var role=" ";
var roleid=" ";
var SFEmail= document.getElementById("email").value;
var ip="http://bluealgo.com";
	//"http://35.200.169.114";
var port="8082";

console.log(Email);
//28-05-19------------------------------------
$(document).ready(function() {
	//alert("working-group-Dropdown ");
	selectworkingroupfun("working-group-Dropdown");
	 selectworkingrole();
	 role=document.getElementById("Display-role").innerHTML;
	 roleid=document.getElementById("Display-roleid").innerHTML;
    // alert("role "+role+" roleid "+roleid);
	// group=document.getElementById("working-group-Dropdown-id").value;
	//alert("group in ready *"+group+"*");
	 if(role=='user' || role=='admin'){
		$('.create-new-clause-btn').attr('disabled',true);
		$('.create-new-template-btn').attr('disabled',true);
		$('.create-new-mail-template-btn').attr('disabled',true);
		    $('.create-new-document-btn').attr('disabled',true);
	}
	
	getClauseTable();
	getTemplateTable();
	getMailTempTable();
	var rdjson=JSON.stringify({"URL":"","Input":[],"Output":[]});
	document.getElementById("RuleData").setAttribute('value', rdjson);
	console.log(document.getElementById("RuleData").value);
	$('.temp-name').attr('id','mailName');	
	if($('.upload_file').is(':checked')){
		$('.upload-file-main').css('display','block');
	}
	if($('.static-dynamic-doc').is(':checked')){
		// call list of temlate to select in Dynamic Document
		//Pallavi  ===============================================
		 // getTempforDoc();
		 //==================================================== 
		$('.select-temp-dynamic-doc-main').css('display','block');
	}
	if($('.upload_file_parent_cls_lib').is(':checked')){
		$('.upload-file-main-parnt-cls-lib').css('display','block');
	}
	//pallavi================
	//alert("in document.ready")
	addeventcomm("selectevent-comm");
	  addattachtemplatecomm("attach-temp-comm");
	 addmailtemplatecomm("select-mail-tmp");
	  addSMStemplatecomm("select-sms-tmp");
		//alert("end set attachtemp")
	//====================
		//pallavi workflowapprovers=====
	document.getElementById("WorkflowApproverType").value="new"
	addApproverDropDown("listofapprovers", "selectwkapprover");
	//=================================
		//pallavi==============
		dislayDocTable();
		//=================
	if($('.upload_file1').is(':checked')){
		$('.upload-file-main1').css('display','block');
	}
	if($('.static-dynamic-doc').is(':checked')){
		// call list of temlate to select in Dynamic Document
		//Pallavi  ===============================================
		  getTempforDoc();
		 //==================================================== 
		$('.select-temp-dynamic-doc-main').css('display','block');
	}
	if($('.upload_file_parent_cls_lib').is(':checked')){
		$('.upload-file-main-parnt-cls-lib').css('display','block');
	}
});
console.log("group *"+group+"*");



$("body").on("change","#working-group-Dropdown-id",function(){
	 group=$(this).val();
	 selectworkingrole();
	 role=document.getElementById("Display-role").innerHTML;
	 roleid=document.getElementById("Display-roleid").innerHTML;
	 //alert("role "+role+" roleid "+roleid);

	 console.log("role in on change group *");
	 if(role=='user' || role=='admin'){
			$('.create-new-clause-btn').attr('disabled',true);
			$('.create-new-template-btn').attr('disabled',true);
			$('.create-new-mail-template-btn').attr('disabled',true);
			    $('.create-new-document-btn').attr('disabled',true);
		}
	 
	 getClauseTable();
		getTemplateTable();
		getMailTempTable();
		var rdjson=JSON.stringify({"URL":"","Input":[],"Output":[]});
		document.getElementById("RuleData").setAttribute('value', rdjson);
		console.log(document.getElementById("RuleData").value);
		$('.temp-name').attr('id','mailName');	
		if($('.upload_file').is(':checked')){
			$('.upload-file-main').css('display','block');
		}
		if($('.static-dynamic-doc').is(':checked')){
			// call list of temlate to select in Dynamic Document
			//Pallavi  ===============================================
			 // getTempforDoc();
			 //==================================================== 
			$('.select-temp-dynamic-doc-main').css('display','block');
		}
		if($('.upload_file_parent_cls_lib').is(':checked')){
			$('.upload-file-main-parnt-cls-lib').css('display','block');
		}
		//pallavi================
		//alert("in document.ready")
		addeventcomm("selectevent-comm");
		  addattachtemplatecomm("attach-temp-comm");
		 addmailtemplatecomm("select-mail-tmp");
		  addSMStemplatecomm("select-sms-tmp");
			//alert("end set attachtemp")
		//====================
			//pallavi workflowapprovers=====
		document.getElementById("WorkflowApproverType").value="new"
		addApproverDropDown("listofapprovers", "selectwkapprover");
		//=================================
			//pallavi==============
			dislayDocTable();
			//=================
		if($('.upload_file1').is(':checked')){
			$('.upload-file-main1').css('display','block');
		}
		if($('.static-dynamic-doc').is(':checked')){
			// call list of temlate to select in Dynamic Document
			//Pallavi  ===============================================
			  getTempforDoc();
			 //==================================================== 
			$('.select-temp-dynamic-doc-main').css('display','block');
		}
		if($('.upload_file_parent_cls_lib').is(':checked')){
			$('.upload-file-main-parnt-cls-lib').css('display','block');
		}
	 
	 
	 
});
//----------------------------------------------
function selectworkingroupfun(selectclass){
	//alert("in selectworkingroupfun");
	// if there is no event it is giving error messahe handle tha and show only create new event option
	$.ajax({ 
	type: 'GET',
	url: 'http://bluealgo.com:8082/portal/process/shoppingcart/service_sampletests?email='+Email,
	async:false,
	success: function (dataa) {
	console.log(dataa);
	var json = JSON.parse(dataa);
	var x = document.getElementsByClassName(selectclass);
	//alert("x.length" +x.length);
	
	for(var i=0; i<x.length; i++){
	//alert("in for "+i);
	x[i].innerHTML="";
	//x[i].options[x[i].options.length] = new Option("--Select Event--", "--Select Event--");
	//x[i].options[x[i].options.length] = new Option("Create New Event", "eventnew");
	if(json.length>0){
	for(var j=0; j<json.length; j++){
	var key =json[j];
	x[i].options[x[i].options.length] = new Option(key, key);
	//x[i].options.add( new Option(key,value) );
	}}else{
		x[i].options[x[i].options.length] = new Option("No Group", "No Group");
	}

	}
	group =document.getElementById("working-group-Dropdown-id").value;
	//alert("group inside "+group);

	}
	});
	}

//=================================================

//alert("group outside "+group);
//alert("group outside 2 "+document.getElementById("working-group-Dropdown-id").value);

//----------------------------------------------
function selectworkingrole(){
	//alert("in selectworkingroupfun");
	// if there is no event it is giving error messahe handle tha and show only create new event option
	$.ajax({ 
	type: 'GET',
	url: 'http://bluealgo.com:8082/portal/servlet/service/getrole_id?Email='+Email+"&group="+group,
	async:false,
	success: function (dataa) {
	console.log(dataa);
	var json = JSON.parse(dataa);
	if(json.hasOwnProperty("role")){
	document.getElementById("Display-role").innerHTML=json.role;
	console.log(" # "+document.getElementById("Display-role").innerHTML);
	//alert("role inside ");
	}
	if(json.hasOwnProperty("roleid")){
		document.getElementById("Display-roleid").innerHTML=json.roleid;
		console.log("# "+document.getElementById("Display-roleid").innerHTML);

		//alert("roleid  inside ");
		}
	}
	});
	}


function getClauseTable(){	
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/DTANewGetClauseList?email='+Email+'&group='+group,
		async:true,
		success:function(dataa){
			try{
				var json=JSON.parse(dataa);
				console.log(json);
				var clsArr=[];
				clsArr=json.Clauses;
				console.log(clsArr);
				/*{"status":"success","Clauses":[{"ClauseId":"1","ClauseName":"sddffdf","Version":"1.1","CreatedBy":"doctiger@xyz.com","ApprovedBy":"Admin",
				*"CreationDate":"MonOct2912:41:54UTC2018","Description":"sddgf","ChildClauses":[{"ChildClauseId":"/content/user/doctiger_xyz.com/DocTigerAdvanced/Clauses/1/ChildClauses/0",
				*"ChildClauseName":"child1","ClauseDescription":"Des1","Version":"1.1","CreatedBy":"doctiger@xyz.com","ApprovedBy":"Admin","CreationDate":"MonOct2912:41:54UTC2018",
				*"ChildClauses":[]},{"ChildClauseId":"/content/user/doctiger_xyz.com/DocTigerAdvanced/Clauses/1/ChildClauses/1","ChildClauseName":"child1",
				*"ClauseDescription":"Des1","Version":"1.1","CreatedBy":"doctiger@xyz.com","ApprovedBy":"Admin","CreationDate":"MonOct2912:41:54UTC2018","ChildClauses":[]}]}]}*/
				var table=document.getElementById("clauses");
				
				table.getElementsByTagName("thead")[0].innerHTML  = table.rows[0].innerHTML; 
				
				var cntChild=1;//1- pre val
				for(var i=0;i<clsArr.length;i++){
					console.log(clsArr.length);
					var clsId= clsArr[i].ClauseId;
					var clsName=clsArr[i].ClauseName;
					var version=clsArr[i].Version;
					var creDate=clsArr[i].CreationDate;
					var ChildClauses= clsArr[i].ChildClauses;
					console.log(ChildClauses);
					if(ChildClauses=="[]"){
						var row=table.insertRow(0);
					}else{
						var row=table.insertRow(cntChild);
					}				
					row.id='row'+i;
					table.rows[cntChild].className="parent-tr";
					var cell1=row.insertCell(0);
					var cell2=row.insertCell(1);
					var cell3=row.insertCell(2);
					var cell4=row.insertCell(3);
					var cell5=row.insertCell(4);
					var cell6=row.insertCell(5);
					var cell7=row.insertCell(6);
					cell7.className="action-btn";
					cell1.innerHTML=clsName+"<i class='fa fa-plus btn btn-sm pull-right cfa-open btn-success' id='chcls'"+i+"cls-id='"+clsId+"' cls-name='"+clsName+"' ch-cls-cnt='"+ChildClauses.length+"'></i>";
					cell2.innerHTML="PDF";
					cell3.innerHTML=Email;
					cell4.innerHTML=Email;
					cell5.innerHTML=creDate;
					cell6.innerHTML=version;
					var rowcnt = document.getElementById("clauses").getElementsByTagName("thead")[0].getElementsByTagName("tr").length;
					console.log(rowcnt);
					cell7.innerHTML='<button class="btn btn-bg-brown btn-sm create-new-clause-btn-edit" id="editCls'+i+'" cls-id="'+clsId+'" cls-name="'+clsName+'"><i class="fa fa-pencil"></i></button><button type="button" class="btn btn-bg-light-danger btn-sm delete-parent-cls" id="del'+i+'" cls-id="'+clsId+'" cls-name="'+clsName+'"><i class="fa fa-trash"></i></button><a href="" class="btn btn-bg-light-bluebtn-sm"><i class="fa fa-eye"></i></a><button class="btn btn-bg-light-danger btn-sm open-add-child-div" id="addChld'+i+'" cls-id="'+clsId+'" cls-name="'+clsName+'" row-cnt="'+rowcnt+'">Add Child</button>';
					cntChild++;
					var childCnt= getChildClstable(clsId, clsName, cntChild, table, ChildClauses);		
					cntChild= childCnt;
					
					document.getElementById("rowcnt").value=rowcnt;
				}
			}catch(err){
				console.log(err);
			}
		}
	});
}

function getChildClstable(clsId, clsName, count, table, ChildClauses){
	console.log(count);
	for(var j=1;j<=ChildClauses.length;j++){
		var chClsId= ChildClauses[j-1].ChildClauseId;
		var chClsName=ChildClauses[j-1].ChildClauseName;
		var version=ChildClauses[j-1].Version;
		var creDate=ChildClauses[j-1].CreationDate;
		var subChildClauses= ChildClauses[j-1].ChildClauses;
		var chrow=table.insertRow(count);
		chrow.id='chrow'+count;
		table.rows[count].className="child-tr";
		var cell1=chrow.insertCell(0);
		var cell2=chrow.insertCell(1);
		var cell3=chrow.insertCell(2);
		var cell4=chrow.insertCell(3);
		var cell5=chrow.insertCell(4);
		var cell6=chrow.insertCell(5);
		var cell7=chrow.insertCell(6);
		cell7.className="action-btn";
		cell1.innerHTML=chClsName+"<i class='fa fa-plus btn btn-sm pull-right cfa-open btn-success' id='chcls'"+j+"cls-id='"+chClsId+"' cls-name='"+chClsName+"'></i>";
		cell2.innerHTML="PDF";
		cell3.innerHTML=Email;
		cell4.innerHTML=Email;
		cell5.innerHTML=creDate;
		cell6.innerHTML=version;
		cell7.innerHTML='<button class="btn btn-bg-brown btn-sm create-new-ch-cls-btn-edit" id="editCls'+j+'" cls-id="'+chClsId+'" cls-name="'+chClsName+'" p-cls-id="'+clsId+'" p-cls-name="'+clsName+'"><i class="fa fa-pencil"></i></button><button type="button" class="btn btn-bg-light-danger btn-sm delete-parent-cls" id="del'+j+'" cls-id="'+chClsId+'" cls-name="'+chClsName+'"><i class="fa fa-trash"></i></button><a href="" class="btn btn-bg-light-blue btn-sm"><i class="fa fa-eye"></i></a><button class="btn btn-bg-light-danger btn-sm open-add-child-div" id="addChld'+j+'" cls-id="'+chClsId+'" cls-name="'+chClsName+'" p-cls-id="'+clsId+'" p-cls-name="'+clsName+'">Add Child</button>';
	count++;
	var chCnt=0;
	if(subChildClauses.length>0){
		chCnt= getChildClstable(chClsId, chClsName, count, table, subChildClauses);
	}
	}	
	//console.log(count+chCnt);
	return count;// previous- return count;
}

$("body").on("click",".btn-success",function(){
	document.getElementById("clsSaveType").value="edit";
	var $this = $(this);
	console.log($this);
	var childCount = $this.attr("ch-cls-cnt");
	console.log(childCount);
	
	var rows = document.getElementById('clauses').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	var rowParent;
    for (i = 0; i < rows.length; i++) {
        rows[i].onclick = function() {
        	rowParent= this.rowIndex + 1;
            //alert(rowParent);
        }
    }
    var x= rowParent+childCount;
    console.log(x);
});

function getTemplateTable(){
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/TemplateList?email='+Email+'&group='+group,
		async: true,
		success:function(dataa){
			var json=JSON.parse(dataa);
			console.log(json);
			var tempArr=[];
			tempArr=json.TemplateList;
			console.log(tempArr);
			if(tempArr=="" || tempArr==null){
				
			}else{
				var table=document.getElementById("templates");
				table.getElementsByTagName("thead")[0].innerHTML  = table.rows[0].innerHTML; 

				for(var i=0;i<tempArr.length;i++){
					var Template=tempArr[i].Template;
					var version=tempArr[i].version;
					var creDate=tempArr[i].Created_Date;
					var action=document.getElementById("action").innerHTML;
					console.log(action);
					var row=table.insertRow(i+1);
					row.id='row'+i;
					table.rows[i+1].className="parent-tr";
					var cell1=row.insertCell(0);
					var cell2=row.insertCell(1);
					var cell3=row.insertCell(2);
					var cell4=row.insertCell(3);
					var cell5=row.insertCell(4);
					var cell6=row.insertCell(5);
					var cell7=row.insertCell(6);
					cell7.className="action-btn";
					cell1.innerHTML=Template;
					cell2.innerHTML="PDF";
					cell3.innerHTML=Email;
					cell4.innerHTML=Email;
					cell5.innerHTML=creDate;
					cell6.innerHTML=version;
					cell7.innerHTML='<button class="btn btn-bg-brown btn-sm create-new-template-btn-edit" id="editTemp'+i+'" temp-name="'+Template+'"><i class="fa fa-pencil"></i></button><button type="button" class="btn btn-bg-light-danger btn-sm delete-temp" id="delTemp'+i+'" temp-name="'+Template+'"><i class="fa fa-trash"></i></button><a href="" class="btn btn-bg-light-blue btn-sm"><i class="fa fa-eye"></i></a><a href="" class="btn btn-bg-blue btn-sm"><i class="fa fa-floppy-o"></i></a>';
				}
			}
/*{"status":"success","TemplateList":[{"Template":"thesaletesttemp","Created_Date":"Fri Oct 12 06:58:34 UTC 2018","Created_By":"doctiger@xyz.com",
 * "Approved_By":"admin","Flag":"1","version":"0.1","description":"xdc","selectedWorkflow":"DoctigerWorkflow"}]}*/	
		}
	});
}

function getMailTempTable(){
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/getMailTemplateList?email='+Email+'&group='+group,
		async: true,
		success:function(dataa){
			try{
				console.log(dataa);
				var json=JSON.parse(dataa);
				console.log(json);
				var mailtempArr=[];
				mailtempArr=json.TemplateList;
				console.log(mailtempArr);
				mailtempArr=  JSON.stringify(mailtempArr).replace(/Creation Date/g, "Creation_Date");
				mailtempArr=  mailtempArr.replace(/Created_Date/g, "Creation_Date");
				/*{"status":"success","TemplateList":[{"templatetype":"mail","Template":"Mail1","Creation Date":"Wed Sep 26 09:46:30 UTC 2018",
				 * "Created_By":"doctiger@xyz.com","Approved_By":"admin","Flag":"1","description":"dfd"}]}*/
				mailtempArr= JSON.parse(mailtempArr);
				var table=document.getElementById("mailTemps");
				
				table.getElementsByTagName("thead")[0].innerHTML  = table.rows[0].innerHTML; 

				for(var i=0;i<mailtempArr.length;i++){
					var tempType= mailtempArr[i].templatetype;
					var Template;
					if(tempType=="mail"){
						Template=mailtempArr[i].Template;
					}else if(tempType=="sms"){
						Template=mailtempArr[i].SMSTemplate;
					}			
					var Flag=mailtempArr[i].Flag;
					var creDate=mailtempArr[i].Creation_Date;
					console.log(creDate);
					var description=mailtempArr[i].description;
					var action=document.getElementById("action").innerHTML;
					var row=table.insertRow(i+1);
					row.id='row'+i;
					table.rows[i+1].className="parent-tr";
					var cell1=row.insertCell(0);
					var cell2=row.insertCell(1);
					var cell3=row.insertCell(2);
					var cell4=row.insertCell(3);
					var cell5=row.insertCell(4);
					var cell6=row.insertCell(5);
					var cell7=row.insertCell(6);
					var cell8=row.insertCell(7);
					cell7.className="action-btn";
					cell1.innerHTML=Template;
					cell2.innerHTML=tempType;
					cell3.innerHTML=Email;
					cell4.innerHTML=Email;
					cell5.innerHTML=creDate;
					cell6.innerHTML=Flag;
					cell7.innerHTML=description;
					cell8.innerHTML='<button class="btn btn-bg-brown btn-sm create-new-mail-template-btn-edit" id="editmailTemp'+i+'" mail-temp-name="'+Template+'" temp-type="'+tempType+'"><i class="fa fa-pencil"></i></button><button type="button" class="btn btn-bg-light-danger btn-sm delete-mailTemp" id="delMailTemp'+i+'" mail-temp-name="'+Template+'" temp-type="'+tempType+'"><i class="fa fa-trash"></i></button><a href="" class="btn btn-bg-light-blue btn-sm"><i class="fa fa-eye"></i></a><a href="" class="btn btn-bg-blue btn-sm"><i class="fa fa-floppy-o"></i></a>';
				}			
			}catch(err){
				console.log(err);
			}
			}
	});
}

var tempType;
$("body").on("change","input[type=radio][name=mail-sms-temp]",function(){
	var checkR=$(this).val();
	if(checkR=='sms'){
		$('.mail-sms-temp-title').html('SMSTemplate');
		tempType="SMS";
		document.getElementById("tempType").value=tempType;
		$('.temp-name').attr('id','smsName');
	}
	if(checkR=='mail'){
		$('.mail-sms-temp-title').html('MailTemplate');
		tempType="Mail";
		document.getElementById("tempType").value=tempType;
		$('.temp-name').attr('id','mailName');
	}
});

$("body").on("click",".click-right-primary-key",function(){
	$(".box-selected-sf-field li.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		$('.primary-key-ext-para-cls-lib').attr('value',liText);
		console.log("heading:"+heading)
		if($(".box-primary-key").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-primary-key").find('h3[data-name="'+heading+'"]').parent("div").find("ul").html("<li>"+liText+"</li>");
		}else{
			$(".box-primary-key").html('<h2 class="box-title">Select Primary Key</h2><div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+liText+'</li></ul></div>');
		}
		var pri_key={};
		pri_key["Object"]=heading;
		pri_key["key"]=liText;
		console.log(pri_key);
		document.getElementById("Pri_Key").value=JSON.stringify(pri_key);
		console.log(document.getElementById("Pri_Key").value);
	});
});

$("body").on("click",".click-left-primary-key",function(){
	$(".box-primary-key li.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		$('.primary-key-ext-para-cls-lib').attr('value','');
		if($(this).parent("ul").parent("div").find("ul li").length==1){
			$(this).parent("ul").parent("div").remove();
		}else{
			$(this).remove();
		}
	});
});

$("body").on("click",".click-right-primary-temp-lib",function(){
	$(".box-right-temp-libli.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		$('.primary-key-ext-para-temp-lib').attr('value',liText);
		if($(".box-primary-temp-lib").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-primary-temp-lib").find('h3[data-name="'+heading+'"]').parent("div").find("ul").html("<li>"+liText+"</li>");
		}else{
			$(".box-primary-temp-lib").html('<h2 class="box-title">Select Primary Key</h2><div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+liText+'</li></ul></div>');
		}
		var pri_key={};
		pri_key["object"]=heading;
		pri_key["key"]=liText;
		console.log(pri_key);
		document.getElementById("Pri_Key").value=JSON.stringify(pri_key);
		console.log(document.getElementById("Pri_Key").value);
	});
});

$("body").on("click",".click-left-primary-temp-lib",function(){
	$(".box-primary-temp-libli.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		$('.primary-key-ext-para-temp-lib').attr('value','');
		if($(this).parent("ul").parent("div").find("ulli").length==1){
			$(this).parent("ul").parent("div").remove();
		}else{
			$(this).remove();
		}
	});
});

$("body").on("click",".click-right-primary-mail-temp",function(){
	$(".box-right-mail-templi.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		$('.primary-key-ext-para-mail-temp').attr('value',liText);
		if($(".box-primary-mail-temp").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-primary-mail-temp").find('h3[data-name="'+heading+'"]').parent("div").find("ul").html("<li>"+liText+"</li>");
		}else{
			$(".box-primary-mail-temp").html('<h2 class="box-title">Select Primary Key</h2><div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+liText+'</li></ul></div>');
		}
	});
});

$("body").on("click",".click-left-primary-mail-temp",function(){
	$(".box-primary-mail-temp li.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		$('.primary-key-ext-para-mail-temp').attr('value','');
		if($(this).parent("ul").parent("div").find("ul li").length==1){
			$(this).parent("ul").parent("div").remove();
		}else{
			$(this).remove();
		}
	});
});								

$("body").on("click",".open-add-child-div",function(){
	$('.create-new-clause-btn').css('display','none');
	$('.table-clause-library-main').css('display','none');
	$('.parent-clause-main').css('display','block');
	document.getElementById("clsSaveType").value="edit";
	var $this = $(this);
	var clauseName = $this.attr("cls-name");
	console.log($this.attr("cls-id"));
	console.log(clauseName);
	document.getElementById("parentCls").value=clauseName;
	document.getElementById("parentClsId").value= $this.attr("cls-id");
});									

$("body").on("click",".add-meta-data-desc-btn",function(){
	$('.meta-data-desc').toggleClass('display-none-bolck');
	$(this).find('i').toggleClass('fa-arrow-up');
	$(this).find('i').toggleClass('fa-arrow-down');
});

$("body").on("click",".add-more-btn-lev2",function(){
	$(this).parents('.addMore-sub').find('.lev1-desc-copy').find('.lev3-desc-copy-app').text('');
	var copy=$(this).parents('.addMore-sub').find('.lev1-desc-copy').html();
	$(this).parents('.addMore-sub').find('.lev1-desc-copy-app').append(copy);
});

$('body').on('click','.copy-remove-btn-lev1',function(){
	$(this).parents('.lev1-desc-sub').remove();
});

$("body").on("click",".add-more-btn-lev3",function(){
	var copy=$(this).parents('.lev1-desc-main').find('.lev3-desc-copy').html();
	$(this).parents('.lev1-desc-main').find('.lev3-desc-copy-app').append(copy);
});

$('body').on('click','.copy-remove-btn-lev3',function(){
	$(this).parents('.lev3-desc-sub').remove();
});

$('.previous-parent-on-title').click(function(){
	$('.parent-clause-main').css('display','none');
	$('.compose-clause-main').css('display','block');
});

$("body").on("click",".dropdown-toggle",function(){
	var Sheight=$(this).parent('.bootstrap-select').find('.dropdown-menu .open').height();
	Sheight=Sheight;
	$('.tab-content').css('padding-bottom',Sheight);
});

$('.addAppWorkflow').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.addMoreTextboxMain').css('display','block');
	}else{
		$('.addMoreTextboxMain').css('display','none');
	}
});

$('.add-more-btn-b').click(function(){
	var copy=$(this).parents('.addMore-b').find('.addMore-copy-b').html();
	console.log(copy);
	$(this).parents('.addMore-b').find('.addMore-main-b').find('.addMoreNew').append(copy);
});

var ii=1;
$('.add-more-btn-b-file').click(function(){
	var copy=$(this).parents('.addMore').find('.addMore-copy').html();
	copy=copy.replace("**1**",ii);
	copy=copy.replace("**1**",ii);
	ii++;
	$(this).parents('.addMore').find('.addMore-main').append(copy);
});

$('body').on('click','.copy-remove-btn-b',function(){
	$(this).parents('.addMore-sub-b').remove();
});

$('.cal-main .plus').click(function(){
	var copy=$(this).parents('.cal-main').find('.copy-cal').html();
	$(this).parents('.cal-main').find('.cal-sub').append(copy);
	$('.selectpicker').selectpicker('refresh');
	$(this).parents('.cal-main').find('.cal-sub').find(".custom-cal-sub:last").find(".bs-placeholder:first").css("display","none");
	$(this).parents('.cal-main').find('.cal-sub').find(".custom-cal-sub:last").find(".sf-remove").find(".bs-placeholder:first").css("display","none");
});

$('.cal-main .minus').click(function(){
	var first=$(this).parents('.cal-main').find('.cal-sub. row:last-child').attr('data-class');
	if(first!='first'){
		$(this).parents('.cal-main').find('.cal-sub. row:last-child').remove();
	}
});

var c1=0;
var c2=0;
var c3=0;
var c4=0;
var c5=0;
var c6=0;
var c7=0;
var c8=0;
var c9=0;
var c10=0;
var c11=0;
var c12=0;

$('body').on('click','.add-more-btn',function(){	
	var para_id= $(this).attr("id");
	console.log(para_id);
	var $this= $(this);
	console.log($this);
	if(para_id=="addcls"){
		console.log(c1);
		c1++;
		console.log("in function addClassnId")
		$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').find('.text-area').addClass('comClsDes').attr('id', composeClsDes+c1);
		var copy=$(this).parents('.addMore').find('.addMore-copy').html();	
		$this.parents('.addMore').find('.addMore-main').append(copy);
		$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').find('.text-area').removeClass('comClsDes').removeAttr('id', composeClsDes+c1);
		$('.selectpicker').selectpicker('refresh');
		$this.parents('.addMore').find('.addMore-sub:last-child').find(".dropdown-toggle:first").css("display","none");
		$this.parents('.addMore').find('.addMore-sub:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
		//addClassnId($this, ".text-area", "comClsDes", "composeClsDes", c1);
		console.log(c1);
	}else if (para_id=="addclsCopy") {
		c2++;
		console.log("in function addClassnId")
		$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').find('.text-area1').addClass('comClsDes1').attr('id', composeClsDesCpy+c2);
		var copy=$(this).parents('.addMore').find('.addMore-copy').html();	
		$this.parents('.addMore').find('.addMore-main').append(copy);
		$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').find('.text-area1').removeClass('comClsDes1').removeAttr('id', composeClsDesCpy+c2);
		$('.selectpicker').selectpicker('refresh');
		$this.parents('.addMore').find('.addMore-sub:last-child').find(".dropdown-toggle:first").css("display","none");
		$this.parents('.addMore').find('.addMore-sub:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
		//addClassnId($this, ".text-area1", "comClsDes1", "composeClsDesCpy", c2);
	}else if(para_id=="addclschld"){
		c3++;
		addClassnId($this, ".text-area2", "comClsDes2", "composeClsDesChld", c3);
	}else if(para_id=="addclschldCopy"){
		c4++;
		addClassnId($this, ".text-area3", "comClsDes3", "comClsDesChldCpy", c4);
	}else if(para_id=="addip"){
		c5++;
		addClassnId1($this, "ipfld", "ip", c5);
	}else if(para_id=="addop"){
		c6++;
		addClassnId1($this, "opfld", "op", c6);
	}else if(para_id=="addtempip"){
		c7++;
		addClassnId1($this, "tempipfld", "temp_ip", c7);
	}else if(para_id=="addtempop"){
		c8++;
		addClassnId1($this, "tempopfld", "temp_op", c8);
	}else if(para_id=="addmailtempip"){
		c9++;
		addClassnId1($this, "mailtempipfld", "mailtemp_ip", c9);
	}else if(para_id=="addmailtempop"){
		c10++;
		addClassnId1($this, "mailtempopfld", "mailtemp_op", c10);
	}else if(para_id=="addsmstempip"){
		c11++;
		addClassnId1($this, "smstempipfld", "smstemp_ip", c11);
	}else if(para_id=="addsmstempop"){
		c12++;
		addClassnId1($this, "smstempopfld", "smstemp_op", c12);
	}		
});	

function addClassnId($this, class_find, class_add, id_add, cntr){
	console.log("in function addClassnId")
	console.log("copy: "+copy+" $this: "+$this+" class_find: "+class_find+" class_add: "+class_add+" id_add: "+id_add+" cntr: "+cntr);
	$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').find('.text-area').addClass(class_add).attr('id', id_add+cntr);
	//console.log($this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').find(class_find).addClass(class_add).attr('id', id_add+cntr));
	var copy=$(this).parents('.addMore').find('.addMore-copy').html();	
	$this.parents('.addMore').find('.addMore-main').append(copy);
	$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').find(class_find).removeClass(class_add).removeAttr('id', id_add+cntr);
	$('.selectpicker').selectpicker('refresh');
	$this.parents('.addMore').find('.addMore-sub:last-child').find(".dropdown-toggle:first").css("display","none");
	$this.parents('.addMore').find('.addMore-sub:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
}

function addClassnId1($this, class_add, id_add, cntr){
	//console.log("copy: "+copy+" $this: "+$this+" class_add: "+class_add+" id_add: "+id_add+" cntr: "+cntr);
	$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').addClass(class_add).attr('id', id_add+cntr);
	var copy=$this.parents('.addMore').find('.addMore-copy').html();	
	$this.parents('.addMore').find('.addMore-main').append(copy);
	$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').removeClass(class_add).removeAttr('id', id_add+cntr);
	$('.selectpicker').selectpicker('refresh');
	$this.parents('.addMore').find('.addMore-sub:last-child').find(".dropdown-toggle:first").css("display","none");
	$this.parents('.addMore').find('.addMore-sub:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
}
var c=0;
$('body').on('click','.add-more-btn-p',function(){
	c++;
	$(this).parents('.addMore-p').find('.addMore-copy-p').find('.addMore-sub-p').addClass("compose-cls").attr('id', 'com-cls'+c);
	var copy=$(this).parents('.addMore-p').find('.addMore-copy-p').html();
	$(this).parents('.addMore-p').find('.addMore-main-p').append(copy);
	$(this).parents('.addMore-p').find('.addMore-copy-p').find('.addMore-sub-p').removeClass("compose-cls").removeAttr('id', 'com-cls'+c);
	$('.selectpicker').selectpicker('refresh');
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child').find(".dropdown-toggle:first").css("display","none");
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
console.log(document.getElementsByClassName("compose-cls"));
});

$('body').on('click','.add-more-btn-p',function(){
	var a1=$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .compose_online').attr('id');
	console.log(a1);
	var b1=Number(a1.substring(13))+Number(1);
	var idc1='composeOnline'+b1;
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .compose_online').attr('id',idc1);
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .compose_online_for').attr('for',idc1);
	var a2=$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .upload_file').attr('id');
	var b2=Number(a2.substring(10))+Number(1);
	var idc2='uploadFile'+b2;
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .upload_file').attr('id',idc2);
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .upload_file_for').attr('for',idc2);
	var a3=$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .upload_file').attr('id');
	var b3=Number(a2.substring(10))+Number(1);
	var name='radio-group'+b3;
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .compose_online').attr('name',name);
	$(this).parents('.addMore-p').find('.addMore-sub-p:last-child .upload_file').attr('name',name);
});

//advanceradiobutton
var childMulLangArr=[];
var mullagjson={};
var cc=0;
$('body').on('click','.add-more-btn-np',function(){
	cc++;
	$(this).parents('.addMore-np').find('.addMore-copy-np').find('.addMore-sub-np').addClass("com-cls-child").attr('id', 'com-cls-chld'+cc);
	var copy=$(this).parents('.addMore-np').find('.addMore-copy-np').html();
	$(this).parents('.addMore-np').find('.addMore-copy-np').find('.addMore-sub-np').removeClass("com-cls-child").removeAttr('id', 'com-cls-chld'+cc);
	$(this).parents('.addMore-np').find('.addMore-main-np').append(copy);
	$('.selectpicker').selectpicker('refresh');
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child').find(".dropdown-toggle:first").css("display","none");
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
	//console.log(document.getElementsByClassName("com-cls-child"));
});

function getMultilingualdata(){	
	var Multilingualdata=[];
	var comCls= document.getElementsByClassName("com-cls-child");
	console.log(comCls);

	for(var i=0; i<comCls.length; i++){
		console.log(cc);
		var id=document.getElementsByClassName('com-cls-child')[i].id;
		console.log(id);
		if(id=="com-cls-chld"){
			var language=document.getElementById("selLang").value;
			var type;
			var mullagjson={};
			mullagjson["language"]=language;
			var para=[];
			if(document.getElementById("ComposeOnline").checked){
				para= getchldClsDes();
				type="online";
				mullagjson["type"]=type;
				mullagjson["para"]=para;
			}else if(document.getElementById("UploadFile").checked){
				type="file";
				mullagjson["type"]=type;
				mullagjson["filename"]= document.getElementById("docFileName").value;
				mullagjson["filedata"]= document.getElementById("docFileData").value;
			}			
			console.log(mullagjson);
			document.getElementById("mullangCom").value=JSON.stringify(mullagjson);
			console.log(document.getElementById("mullangCom").value);
			Multilingualdata.push(JSON.parse(document.getElementById("mullangCom").value));			
		}else{
			
			console.log(cc);
			var language=document.getElementById("selLangComposeCpy").value;
			var type;
			var mullagjson={};		
			mullagjson["language"]=language;
			var para1=[];
			//var selected_id = $("input[name='radio-Group3']:checked").attr("id");
			var selected_id= $('input:radio:checked').attr('id');
			console.log(selected_id);
			if(document.getElementById(idc1).checked){
				para1= getchldClsDes1();
				type="online";
				mullagjson["type"]=type;
				mullagjson["para"]=para1;
			}else if(document.getElementById(idc2).checked){
				type="file";
				mullagjson["type"]=type;
				mullagjson["fileName"]="";
				mullagjson["fileData"]="";
			}
			cc++;
			console.log(mullagjson);
			document.getElementById("mullangCom").value=JSON.stringify(mullagjson);
			console.log(document.getElementById("mullangCom").value);
			Multilingualdata.push(JSON.parse(document.getElementById("mullangCom").value));
		}
	}
	console.log(Multilingualdata);
	
	/*mullagjson["language"]=language;
	mullagjson["type"]=type;
	mullagjson["para"]=para;
	console.log(mullagjson);*/
	
	return Multilingualdata;
}

function getchldClsDes(){
	var para=[];
	console.log("in function");
	var paracls= document.getElementsByClassName("comClsDes2");
	console.log(paracls);
	for(var i=0; i<paracls.length; i++){
		var comment = paracls[i].value;
		console.log(comment);
		para.push(comment);
	}				
	return para;
}

function getchldClsDes1(){
	var para=[];
	console.log("in function1");
	var paracls= document.getElementsByClassName("comClsDes3");
	console.log(paracls);
	for(var i=0; i<paracls.length; i++){
		var comment = paracls[i].value;
		console.log(comment);
		para.push(comment);
	}				
	return para;
}

var idc1;
var idc2;
$('body').on('click','.add-more-btn-np',function(){
	console.log("test");
	var a1=$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .compose_online').attr('id');
	console.log(a1);
	//var name_radio= $(this).parents('.addMore-np').find('.addMore-sub-np').find('.addMore-b').find('.radio').attr('name');
	var name_radio= $(this).parents('.addMore-np').find('.addMore-sub-np').find('.addMore-b').find('.radio').value;
	//console.log(name_radio);
	var radios = $("input[type='radio']");
	console.log($("input:radio.com-cls-child:checked"));
	console.log($('input:radio:checked').attr('id'));
	var b1=Number(a1.substring(13))+Number(1);
	idc1='ComposeOnline'+b1;
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .compose_online').attr('id',idc1);
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .compose_online_for').attr('for',idc1);
	var a2=$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .upload_file').attr('id');
	var b2=Number(a2.substring(10))+Number(1);
	idc2='UploadFile'+b2;
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .upload_file').attr('id',idc2);
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .upload_file_for').attr('for',idc2);
	var a3=$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .upload_file').attr('id');
	var b3=Number(a3.substring(10))+Number(1);
	var name='radio-Group'+b3;
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .compose_online').attr('name',name);
	$(this).parents('.addMore-np').find('.addMore-sub-np:last-child .upload_file').attr('name',name);
});

$('body').on('click','.copy-remove-btn',function(){
	$(this).parents('.addMore-sub').remove();
});

$('.create-new-clause-btn').click(function(e){
	e.preventDefault();
	$('.create-new-clause-btn').css('display','none');
	$('.createNewClauseMain').css('display','block');
	$('.table-clause-library').css('display','none');
	$('.compose-clause-main').css('display','none');
	$('.select-sfdc-object-main').css('display','none');
	$('.external-parameter-main').css('display','none');
	
	document.getElementById("clsSaveType").value= "new";
});

$('input[type="checkbox"]').click(function(){
    if($(this).prop("checked") == true){
        //alert("Checkbox is checked.");
        document.getElementById("external-parameter").value="true";
        document.getElementById("check-external-parameter-temp").value="true";
        document.getElementById("external-parameter-mail-temp").value="true";
    }
    else if($(this).prop("checked") == false){
        //alert("Checkbox is unchecked.");
        document.getElementById("external-parameter").value="false";
        document.getElementById("check-external-parameter-temp").value="false";
        document.getElementById("external-parameter-mail-temp").value="false";
    }
});

var returnedData=null;
var result;
var mainJson={};
$('.clause-library-save-next').click(function saveClause(){
	/*{"Email":"doctiger@xyz.com","UserName":"user@gmail.com", "ClauseName":"C1","Metadata":"MD1","ExternalParam":"true",
	*"Description":"D1","savetype":"new","SFEmail":""}*/
	var UserName="doctiger@xyz.com";
	var ClauseName=document.getElementById("cn").value;
	var MetaData=document.getElementById("md").value;
	var ExternalParam=document.getElementById("external-parameter").value;
	var Description=document.getElementById("de").value;
	var savetype=document.getElementById("clsSaveType").value;
	if(savetype=="edit"){
		mainJson["ClauseId"]=document.getElementById("ParentClauseId").value;
	}
	mainJson["Email"]=Email;
	mainJson["group"]=group;
	mainJson["UserName"]=UserName;
	mainJson["ClauseName"]=ClauseName;
	mainJson["Metadata"]=MetaData;
	mainJson["ExternalParam"]=ExternalParam;
	mainJson["Description"]=Description;
	mainJson["savetype"]=savetype;
	mainJson["SFEmail"]=SFEmail;
	console.log(JSON.stringify(mainJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/DTANewSaveParent.basic',
		async:true,
		data:JSON.stringify(mainJson),
		contentType:"application/json",
		success:function(dataa){
			console.log(dataa);
			var json=JSON.parse(dataa);
			var status= json.status;
			if(status=="success"){
				var ClauseId=json.ClauseId;
				document.getElementById("ParentClauseId").value=ClauseId;
				console.log(document.getElementById("ParentClauseId").value);
				$('.createNewClauseMain').css('display','none');
				$('.select-sfdc-object-main').css('display','block');
			}else if(status=="error"){
				//alert(json.message);
				$('.createNewClauseMain').css('display','block');
			}			
		}
	});
});

/*
  
 */
//id="dwldlink" to download template need to change
$('.downloadtemplate').click(function getlink() {
	console.log("link dwldtemp= " + dwldtemp);

	var a = document.getElementById('downldtemplink'); // or grab it by tagname etc
	a.href = dwldtemp;

});

$('body').on('click','.upload_file',function(){
	var $this=$(this);
	if($this.is(':checked')){
		$(this).parents('.addMore-sub-np').find('.addMore-b').find('.upload-file-main').css('display','block');
		$(this).parents('.addMore-sub-np').find('.addMore-b').find('.compose-online-main').css('display','none');
	}
});

$('body').on('click','.upload_file',function(){
	var $this=$(this);
	if($this.is(':checked')){
		$(this).parents('.addMore-sub-p').find('.addMore-b').find('.upload-file-main').css('display','block');
		$(this).parents('.addMore-sub-p').find('.addMore-b').find('.compose-online-main').css('display','none');
	}
});

$('body').on('click','.compose_online',function(){
	var $this=$(this);
	if($this.is(':checked')){
		$(this).parents('.addMore-sub-np').find('.addMore-b').find('.compose-online-main').css('display','block');
		$(this).parents('.addMore-sub-np').find('.addMore-b').find('.upload-file-main').css('display','none');
	}
});

$('body').on('click','.compose_online',function(){
	var $this=$(this);
	if($this.is(':checked')){
		$(this).parents('.addMore-sub-p').find('.addMore-b').find('.compose-online-main').css('display','block');
		$(this).parents('.addMore-sub-p').find('.addMore-b').find('.upload-file-main').css('display','none');
	}
});

$('.advanced').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.advanced-main').css('display','block');
	}else{
		$('.advanced-main').css('display','none');
	}
	if($this.is(':checked')){
		if($('#rules-based').is(':checked')){
			$('.select-rule-clause-lib').css('display','block');
		}
	}else{
		$('.select-rule-clause-lib').css('display','none');
	}
});

$('.advanced-dynamic-doc').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		//pallavi=======================
 		getworkflowfordoc( "workflowforDynamic",  "workflowforDoc");
 		getcontrollerfordoc( "controllerforDynamic",  "controllerforDoc");
 		
 		//===========================
		$('.dynamic-doc-controle-name-main').css('display','block');
	}else{
		$('.dynamic-doc-controle-name-main').css('display','none');
	}
});

$('.clause-library-add-new').click(function(){
	$(".wizard-navigation ul li a .tab-workflow").trigger("click");
});

//pallavi2

$("body").on("change", "#showpendingApprovers", function(){
	var option = $(this).val();
	if(option == "pendingApproval"){
	window.open(ip+':'+port+'/portal/servlet/service/pendingApproval');
	//console.log("request.getcontextpath()"+request.getContextPath());

}else{

	}
});

$("body").on("change","#selectEvent-communication",function(){
	var option=$(this).val();
	if(option=="eventnew"){
		//pallavi=================================
  		document.getElementById("comm-event-savetype").value="new";
  		//=======================
		$('#communication .eventold').css('display','none');
		$('#communication .eventnew').css('display','block');
	}else{
  		//pallavi================
  		document.getElementById("comm-event-savetype").value="edit";
           //callapi to display event data
  		
  	    var eventname =$(this).find('option:selected').text();
  		var  eventid=$(this).val();
  		
  		console.log("eventname "+eventname +"eventid "+eventid);
  		//directly add function code here    		
  		
//{"status":"success","Email":"doctiger_xyz.com","Eventname":"EN13","SendToCMS":"true","savetype":"edit",
//"Data":[{"CTId":"0","CT":"Mail,SMS,Download","MailTempName":"Temp4nn","smsTempName":"SMStemp","AttachedTempName":"TestTemp",
//"AttachedTempType":"TemplateLibraty"},{"CTId":"1","CT":"Mail,Download","MailTempName":"Temp4nn","AttachedTempName":"TestTemp22",
//"AttachedTempType":"AdvancedTemplate"},{"CTId":"2","CT":"SMS","smsTempName":"SMS1","AttachedTempName":"TestTemp1","AttachedTempType":""}]}
//	
	console.log("in edit event");
	console.log('url  portal/servlet/service/EditEventList?email='+Email+'&eventId='+eventid+'&eventName='+eventname);
	 $.ajax({
		   type: 'GET',
		   url: '/portal/servlet/service/EditEventList?email='+Email+'&eventId='+eventid+'&eventName='+eventname+'&group='+group,
		   async: true,
		   success: function (dataa) {
		    console.log(dataa);
		    ////alert("dataa  "+JSON.stringify(dataa));

		    var json = JSON.parse(dataa);
		    var dataarray = json.Data;
		    
		   console.log("dataarray  "+JSON.stringify(dataarray));
		   
	 for(var i=0; i<dataarray.length; i++){
		 
		 var CTid=dataarray[i].CTId;
		 var CT=dataarray[i].CT;
		 
		 console.log("CTid "+CTid );
		 console.log("CT "+CT );
		 		 
		 if(i==0){
//step1 get content of main 
			// console.log("1* "+$(this).html());
			 console.log("1* "+$(this).parents('.communicationtwoclass').html());

	 var main= 	$(this).parents('.communicationtwoclass').find(".eventold").find('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub');
//do processing - get html
	 main.find('.comunication-sectioin-last').attr("CTId",dataarray[i].CTId);
	 var CTarr=dataarray[i].CT.split(',');
	 for(var j=0; j<CTarr.length; j++){
		 if(CTarr[j]=="Mail"){
			 var MailTempName=dataarray[i].MailTempName;
			 console.log("MailTempName "+MailTempName);

			 main.find('.select-mail-tmp').val(dataarray[i].MailTempName);
	// add in copy  main.find('.select-mail-tmp').addClass('select-mail-tmp-d')
		 }
		 if(CTarr[j]=="SMS"){
			 var smsTempName= dataarray[i].smsTempName
			 console.log("smsTempName "+smsTempName );

			 main.find('.select-sms-tmp').val(dataarray[i].smsTempName);
	// add in copy  main.find('.select-sms-tmp').addClass('select-sms-tmp-d')
	      }
		 var AttachedTempType= dataarray[i].AttachedTempType
		 var AttachedTempType = dataarray[i].AttachedTempType
console.log("*** "+main.find('.change-attach').html());
		 main.find('.change-attach').val(dataarray[i].AttachedTempType); 
		 main.find('.change-attach').text(dataarray[i].AttachedTempName);
	 console.log("AttachedTempType "+AttachedTempType);
	 console.log("AttachedTempType "+AttachedTempType);

	 }
	 var mainhtml=$(this).parents('.communicationtwoclass').find(".eventold").find('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub').html();
	 console.log("mainhtml*1*  "+mainhtml);
	// make it empty 
	 document.getElementById("eventoldmaindiv").innerHtml="";
	 //again append html
	 $(this).parents('.communicationtwoclass').find(".eventold").find('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub').html(mainhtml);
	 console.log("mainhtml*2*  "+$(this).parents('.communicationtwoclass').find(".eventold").find('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub').html());

	 }else{
		 
		 // get copy content
		 var copy = $(this).parents('.communicationtwoclass').find(".eventold").find('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub').html();

		 //do processing
		 //append to main
	 }
		   }
		   }
	  }); 
  		
  		//displayeventcontent( eventname, eventid)
  		//====================
		$('#communication .eventnew').css('display','none');
		$('#communication .eventold').css('display','block');
	}
});

$("body").on("change", "select.selectpicker.eventnew-sel-com", function(){
    var option = $(this).val();

    if(option == "mail"){
      $(this).parents('.row-c').find('.select-mail-tmp').removeClass('select-mail-tmp-d');
      $(this).parents('.row-c').find('.select-sms-tmp').addClass('select-sms-tmp-d');
      $(this).parents('.row-c').find('.select-clist-tmp').addClass('select-clist-tmp-d');
    }else if(option == "sms"){
      $(this).parents('.row-c').find('.select-sms-tmp').removeClass('select-sms-tmp-d');
      $(this).parents('.row-c').find('.select-mail-tmp').addClass('select-mail-tmp-d');
      $(this).parents('.row-c').find('.select-clist-tmp').addClass('select-clist-tmp-d');
    }else if(option == "calling-list"){
      $(this).parents('.row-c').find('.select-clist-tmp').removeClass('select-clist-tmp-d');
      $(this).parents('.row-c').find('.select-mail-tmp').addClass('select-mail-tmp-d');
      $(this).parents('.row-c').find('.select-sms-tmp').addClass('select-sms-tmp-d');
    }else if(option == "mail,sms"){
      $(this).parents('.row-c').find('.select-mail-tmp').removeClass('select-mail-tmp-d');
      $(this).parents('.row-c').find('.select-sms-tmp').removeClass('select-sms-tmp-d');
      $(this).parents('.row-c').find('.select-clist-tmp').addClass('select-clist-tmp-d');
    }else if(option == "mail,calling-list"){
      $(this).parents('.row-c').find('.select-mail-tmp').removeClass('select-mail-tmp-d');
      $(this).parents('.row-c').find('.select-clist-tmp').removeClass('select-clist-tmp-d');
      $(this).parents('.row-c').find('.select-sms-tmp').addClass('select-sms-tmp-d');
    }else if(option == "sms,calling-list"){
      $(this).parents('.row-c').find('.select-sms-tmp').removeClass('select-sms-tmp-d');
      $(this).parents('.row-c').find('.select-clist-tmp').removeClass('select-clist-tmp-d');
      $(this).parents('.row-c').find('.select-mail-tmp').addClass('select-mail-tmp-d');
    }else if(option == "mail,sms,calling-list"){
      $(this).parents('.row-c').find('.select-mail-tmp').removeClass('select-mail-tmp-d');
      $(this).parents('.row-c').find('.select-sms-tmp').removeClass('select-sms-tmp-d');
      $(this).parents('.row-c').find('.select-clist-tmp').removeClass('select-clist-tmp-d');      
    }else{
      $(this).parents('.row-c').find('.select-mail-tmp').addClass('select-mail-tmp-d');
      $(this).parents('.row-c').find('.select-sms-tmp').addClass('select-sms-tmp-d');
      $(this).parents('.row-c').find('.select-clist-tmp').addClass('select-clist-tmp-d');      
    }
});

$("body").on("change",".eventnew .schedule_push",function(){
	var val=$(this).attr('value');
	if(val=="push"){
		$('.schedule_push_hideShow').css('display','none');
	}else{
		$('.schedule_push_hideShow').css('display','block');
	}
});

$('.comunication-sectioin-main-last .plus').click(function(){
	var copy=$(this).parents('.comunication-sectioin-main-last').find('.comunication-sectioin-main-copy-sub').html();
	$(this).parents('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub').append(copy);
	$('.selectpicker').selectpicker('refresh');
	$(this).parents('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub').find('.row:last').find('.form-group').find(".dropdown-toggle:first").css("display","none");
});

$('.comunication-sectioin-main-last .minus').click(function(){
	var first=$(this).parents('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub .row:last-child').attr('data-class');
	if(first!='first'){
		$(this).parents('.comunication-sectioin-main-last').find('.comunication-sectioin-main-last-sub .row:last-child').remove();
	}
});

$('.create-new-template-btn').click(function(e){
	e.preventDefault();
	$('.create-new-template-btn').css('display','none');
	$('.createNewTempMain').css('display','block');
	$('.table-clause-template').css('display','none');
	$('.compose-clause-main-template').css('display','none');
	$('.select-sfdc-object-main-template').css('display','none');
	$('.external-parameter-main-template').css('display','none');
});									

var result;
var tempmainJson={};
$('.template-library-save-next').click(function(){
	/*{"templatename":"temp1","metadata":"m1","email":"doctiger@xyz.com","username":"user@gmail.com",
	"description":"descr","externalparam":"true","saveType":"new","SFEmail":""}*/
	var UserName="doctiger@xyz.com";
	var templatename=document.getElementById("tempName").value;
	var MetaData=document.getElementById("tempMD").value;
	var ExternalParam=document.getElementById("check-external-parameter-temp").value;
	console.log(ExternalParam);
	var Description=document.getElementById("tempDes").value;
	var savetype= document.getElementById("tempSaveType").value;
	tempmainJson["email"]=Email;
	tempmainJson["group"]=group;
	tempmainJson["username"]=UserName;
	tempmainJson["templatename"]=templatename;
	tempmainJson["metadata"]=MetaData;
	tempmainJson["externalparam"]=ExternalParam;
	tempmainJson["description"]=Description;
	tempmainJson["saveType"]=savetype;
	tempmainJson["SFEmail"]=SFEmail;
	//alert(JSON.stringify(tempmainJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/SaveTemplate.Basic',
		async:true,
		data:JSON.stringify(tempmainJson),
		contentType:"application/json",
		success:function(tempdata){
			//alert(tempdata);
			console.log(tempdata);
			var json=JSON.parse(tempdata);
			var tempName=json.templatename;
			document.getElementById("tempName").value=tempName;
			console.log(document.getElementById("tempName").value);
			$('.createNewTempMain').css('display','none');
			$('.select-sfdc-object-main-template').css('display','block');
		}
	});
});

$('.create-new-mail-template-btn').click(function(e){
	e.preventDefault();
	$('.create-new-mail-template-btn').css('display','none');
	$('.createNewMailTempMain').css('display','block');
	$('.table-mail-template').css('display','none');
	$('.compose-clause-mail-temp-main').css('display','none');
	$('.select-sfdc-object-mail-temp-main').css('display','none');
	$('.external-parameter-mail-temp-main').css('display','none');	
	
	var mailSmsTemp=$('input[name=mail-sms-temp]:checked').val();
	if(mailSmsTemp=='mail'){
		tempType="Mail";
		document.getElementById("tempType").value=tempType;
		$('.temp-name').attr('id','mailName');
	}
	if(mailSmsTemp=='sms'){
		tempType="SMS";
		document.getElementById("tempType").value=tempType;
		$('.temp-name').attr('id','smsName');
	}
	console.log(tempType);
});

var result;
var mailJson={};
$('.mail-template-save-next').click(function(){
	/*{"saveType":"new","email":"doctiger@xyz.com","mailtemplatename":"Mailtemp","Metadata":"m1","ExternalParam":"true",
	*"Description":"Des1"}*/
	/*{"saveType":"new","email":"doctiger@xyz.com","SFEmail":"doctiger8@gmail.com", "smstemplatename":"SMS1temp", "Metadata":"m1", 
	 * "ExternalParam":"true", "Description":"Des1"}*/
	
	var tempName;
	var Id_name = $('.temp-name').attr('id');
	
	if(Id_name=="mailName"){
		var mailtemplatename=document.getElementById("mailName").value;
		saveTemp("mailtemplatename",mailtemplatename,"DTAMailTemp.basic");
	}
	else if(Id_name=="smsName"){
		var smsTempName=document.getElementById("smsName").value;
		saveTemp("smstemplatename",smsTempName,"SaveSMSTemplate.Basic");	
	}	
});

function saveTemp(templatename, tempName, url){
	var UserName="doctiger@xyz.com";
	//var mailtemplatename=document.getElementById("mailName").value;
	var MetaData=document.getElementById("mailTempMD").value;
	var Description=document.getElementById("mailTempDes").value;
	var savetype=document.getElementById("mailtempSaveType").value;
	mailJson["email"]=Email;
	mailJson["group"]=group;
	mailJson["SFEmail"]=SFEmail;
	mailJson[templatename]=tempName;
	mailJson["Metadata"]=MetaData;
	mailJson["ExternalParam"]=ExternalParam;
	mailJson["Description"]=Description;
	mailJson["saveType"]=savetype;

	//alert(JSON.stringify(mailJson));
	console.log(JSON.stringify(mailJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/'+url,
		async:true,
		data:JSON.stringify(mailJson),
		contentType:"application/json",
		success:function(dataa){
			console.log(dataa);
			var json=JSON.parse(dataa);
			var status= json.status;
			if(status=="success"){
				var mailTempName=json.mailtemplatename;
				document.getElementById("mailTempName").value=mailTempName;
				console.log(document.getElementById("mailTempName").value);
				var smsTempName= json.SMStemplatename;
				document.getElementById("smsTempName").value=smsTempName;
				console.log(document.getElementById("smsTempName").value);
				$('.createNewMailTempMain').css('display','none');
				$('.select-sfdc-object-mail-temp-main').css('display','block');				
			}else if(status=="error"){
				//alert(json.message);
				$('.createNewMailTempMain').css('display','block');
			}
		}
	});
}

$('.upload_file_mail_temp').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.upload-file-mail-temp-main').css('display','block');
		$('.compose-online-mail-temp-main').css('display','none');
	}
});

$('.compose_online_mail_temp').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.compose-online-mail-temp-main').css('display','block');
		$('.upload-file-mail-temp-main').css('display','none');
	}
});

$('.advanced_mail_temp').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.advanced-mail-temp-main').css('display','block');
	}else{
		$('.advanced-mail-temp-main').css('display','none');
	}
});

var sfmainJson={};
$('.cls-lib-sfdc-save-next').click(function(){
	$('.select-sfdc-object-main').css('display','none');
	if($('#external-parameter').is(':checked')){
		$('.external-parameter-main').css('display','block');
		$('.compose-clause-main').css('display','none');
	}else{
		$('.compose-clause-main').css('display','block');
		$('.external-parameter-main').css('display','none');
	}
	/*{"Email":"doctiger@xyz.com","SFEmail":"user1@gmail.com","savetype":"new/edit","ClauseId":"24","ClauseName":"C12",
	*"SFobject":{"Account":["accname","Accno"],"contact":["contactname","contactno"]},"PrimaryKey":{"Object":"Account","key":"Accno"}}*/
	/*var savetype="new";
	var UserName="doctiger@xyz.com";
	var ClsId=document.getElementById("ParentClauseId").value;
	var ClauseId="/content/user/"+Email.replace("@","_")+"/DocTigerAdvanced/Clauses/"+ClsId;
	var ClauseName=document.getElementById("cn").value;
	var jsonsfobj=JSON.parse(document.getElementById("SFObject").value);
	var sfobj=jsonsfobj.SFobject;
	var sel_SFFields=document.getElementById("sel_SFFields").value;
	var sfArr=[];
	var pri_key=JSON.parse(document.getElementById("Pri_Key").value);
	console.log(sfobj);
	sfmainJson["Email"]=Email;
		sfmainJson["group"]=group;
	sfmainJson["SFEmail"]=SFEmail;
	sfmainJson["savetype"]=savetype;
	sfmainJson["UserName"]=UserName;
	sfmainJson["ClauseId"]=ClauseId;
	sfmainJson["ClauseName"]=ClauseName;
	sfmainJson["PrimaryKey"]=pri_key;
	sfmainJson["SFobject"]=sfobj;
	//alert(JSON.stringify(sfmainJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/DTANewSaveParent.sfobj',
		async:true,
		data:{
			result:JSON.stringify(sfmainJson)
		},
		datatype:"json",
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			var ClauseId=json.ClauseId;
			document.getElementById("ParentClauseId").value=ClauseId;
			console.log(document.getElementById("ParentClauseId").value);
			if($('#external-parameter').is(':checked')){
				$('.external-parameter-main').css('display','block');
				$('.compose-clause-main').css('display','none');
			}else{
				$('.compose-clause-main').css('display','block');
				$('.external-parameter-main').css('display','none');
			}
		}
	});*/
});

var exmainJson={};
$('.cls-lib-external-para-save-next').click(function(){
/*{"Email":"doctiger@xyz.com","SFEmail":"user1@gmail.com","savetype":"new", "ClauseId":"24","ClauseName":"C12","externalparamobject":
 * [{"type":"ws","primerykey":"Accno","input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":
 * "amount","fieldtype":"string","fieldlength":"255"}],	"url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1",
 * "password":"pass"}]}*/
	var savetype="new";
	var ClsId=document.getElementById("ParentClauseId").value;
	var ClauseId= ClsId;
	var ClauseName=document.getElementById("cn").value;
	var type="";
	var primerykey="";
	exmainJson["Email"]=Email;
	exmainJson["group"]=group;
	exmainJson["SFEmail"]=SFEmail;
	exmainJson["savetype"]=savetype;
	exmainJson["ClauseId"]=ClauseId;
	exmainJson["ClauseName"]=ClauseName;
	var externalparamobject=document.getElementById("exparamArr").value;
	console.log(document.getElementById("exparamArr").value);
	if(externalparamobject==null || externalparamobject== ""){
		var url=document.getElementById("web-services-url").value;
		var token=document.getElementById("token").value;
		var username=document.getElementById("user").value;
		var password=document.getElementById("pass").value;
		if(url==""||token==""||username==""||password==""){
			//alert("All fields must be filled");
			$('.external-parameter-main').css('display','block');
		}else{
			var webservice= getExparamObject(url, token, username, password);
			exparamArr.push(webservice);
			console.log(exparamArr);
			console.log("test");
			exmainJson["externalparamobject"]=exparamArr;
			saveExparam(exmainJson);
		}		
	}else{
		exmainJson["externalparamobject"]=JSON.parse(document.getElementById("exparamArr").value);
		saveExparam(exmainJson);
	}	
});

function saveExparam(exmainJson){
	console.log(JSON.stringify(exmainJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/DTANewSaveParent.exparam',
		async: true,
		data:JSON.stringify(exmainJson),
		contentType:"application/json",
		success:function(dataa){
			//alert(dataa);
			console.log(dataa);
			var json=JSON.parse(dataa);
			var ClauseId=json.ClauseId;
			document.getElementById("ParentClauseId").value=ClauseId;
			console.log(document.getElementById("ParentClauseId").value);
			$('.external-parameter-main').css('display','none');
			$('.compose-clause-main').css('display','block');
		}
	});
}

$('#rules-based').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		if($('.advanced').is(':checked')){
			$('.select-rule-clause-lib').css('display','block');
		}
	}else{
		$('.select-rule-clause-lib').css('display','none');
	}
});

$('#approval-workflow').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-workflow-clause-lib').css('display','block');
	}else{
		$('.select-workflow-clause-lib').css('display','none');
	}
});

var composeJson={};
//var cntCompose=0;
$('.compose-clause-save-btn').click(function(){
	/*http://35.221.183.246:8082/portal/servlet/service/DTANewSaveParentClses.compose
	*{"Email":"doctiger@xyz.com","savetype":"new","ClauseId":"0","ClauseName":"TheSale","Advanced":"true",
	"Multilingualdata":[{"language":"English","type":"online","para":["kujhgcuxdgfudgfe7r6er","mncbvxcbvdjbvdbfiwryi3"]},
	{"language":"Arabic","type":"online","para":["kujhgcuxdgfudgfe7r6er","mncbvxcbvdjbvdbfiwryi3"]}],"WorkFlow":"DoctigerWorkflow",
	"RuleBased":"RB1","Ruledata":{"URL":"http://35.236.213.87:8080/drools/callrules/doctiger@xyz.com_doctiger_project_rulesdoctiger/fire",
	"Input":[],"Output":[]},"Controller":"Controller1"}*/
	var savetype="new";
	var ClsId=document.getElementById("ParentClauseId").value;
	var ClauseId= ClsId;
	var ClauseName=document.getElementById("cn").value;
	var comCls= document.getElementsByClassName("compose-cls");
	console.log(comCls);
	var Multilingualdata=[];
	
	for(var i=0; i<comCls.length; i++){
		console.log(c);
		var id=document.getElementsByClassName('compose-cls')[i].id;
		console.log(id);
		if(id=="com-cls"){
			var language=document.getElementById("selLangCompose").value;
			var type;
			var mullagjson={};
			mullagjson["language"]=language;
			var para=[];
			if(document.getElementById("ComposeOL").checked){
				para= getClsDes();
				type="online";
				mullagjson["type"]=type;
				mullagjson["para"]=para;
			}else if(document.getElementById("UpFileCompose").checked){
				type="file";
				mullagjson["type"]=type;
				mullagjson["filename"]= document.getElementById("docFileName").value;
				mullagjson["filedata"]= document.getElementById("docFileData").value;
			}			
			console.log(mullagjson);
			document.getElementById("mullangCom").value=JSON.stringify(mullagjson);
			console.log(document.getElementById("mullangCom").value);
			Multilingualdata.push(JSON.parse(document.getElementById("mullangCom").value));			
		}else{
			var language=document.getElementById("selLangComposeCpy").value;
			var type;
			var mullagjson={};		
			mullagjson["language"]=language;
			var para1=[];
			if(document.getElementById("composeOnline"+c).checked){
				para1= getClsDes1();
				type="online";
				mullagjson["type"]=type;
				mullagjson["para"]=para1;
			}else if(document.getElementById("uploadFile"+c).checked){
				type="file";
				mullagjson["type"]=type;
				mullagjson["fileName"]="";
				mullagjson["fileData"]="";
			}
			c++;
			console.log(mullagjson);
			document.getElementById("mullangCom").value=JSON.stringify(mullagjson);
			console.log(document.getElementById("mullangCom").value);
			Multilingualdata.push(JSON.parse(document.getElementById("mullangCom").value));
		}
	}
	console.log(Multilingualdata);
	var WorkFlow=document.getElementById("wf_sel").value;
	var RuleBased=document.getElementById("RuleEngine").value;
	var Ruledata=document.getElementById("RuleData").value;
	console.log(Ruledata);
	var Controller=document.getElementById("Con").value;
	composeJson["Email"]=Email;
	composeJson["group"]=group;
	composeJson["savetype"]=savetype;
	composeJson["ClauseId"]=ClauseId;
	composeJson["ClauseName"]=ClauseName;
	composeJson["Multilingualdata"]=Multilingualdata;
	composeJson["WorkFlow"]=WorkFlow;
	composeJson["RuleBased"]=RuleBased;
	composeJson["Ruledata"]=Ruledata;
	composeJson["Controller"]=Controller;
	//alert(JSON.stringify(composeJson));
	console.log(JSON.stringify(composeJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/DTANewSaveParent.compose',
		async:true,
		data:JSON.stringify(composeJson),
		contentType:"application/json",
		success:function(dataa){
			//alert(dataa);
			console.log(dataa);
			alert("Clause saved successfully");
			document.getElementById("clauses").innerHTML="";
			getClauseTable();
			/*$('.createNewClauseMain').css('display','none');
			$('.select-sfdc-object-main').css('display','none');
			$('.external-parameter-main').css('display','none');
			$('.compose-clause-main').css('display','none');
			$('.table-clause-library').show();
			$('.table-clause-library-main').css('display','block');
			$('.create-new-clause-btn').css('display','block');*/
			window.location.reload();
		}
	});	
});

function getClsDes(){
	var para=[];
	console.log("in function");
	var paracls= document.getElementsByClassName("comClsDes");
	console.log(paracls);
	for(var i=0; i<paracls.length; i++){
		var comment = paracls[i].value;
		console.log(comment);
		para.push(comment);
	}				
	return para;
}

function getClsDes1(){
	var para=[];
	console.log("in function1");
	var paracls= document.getElementsByClassName("comClsDes1");
	console.log(paracls);
	for(var i=0; i<paracls.length; i++){
		var comment = paracls[i].value;
		console.log(comment);
		para.push(comment);
	}				
	return para;
}

var saveChildJson={};
$('.child-clause-save-btn').click(function(){
	//http://35.221.183.246:8082/portal/servlet/service/SaveChildClases
	/*{"Email":"doctiger@xyz.com","SFEmail":"user1@gmail.com","savetype":"new","ParentClauseId":"6/ChildClauses/0","ParentClauseName":"caluse1434w2",
	*"ChildArray":[{"ClauseName":"GENa","DisplayName":"True","Metadata":"GENa","Description":"GENa","Multilingualdata":[{"language":"English","type":"online",
	*"para":["theUnitisnotowned,developedorsoldbytheLicensor."]},{"language":"Arabic","type":"online","para":["أنالوحدومملوكةومطورةومباعةمنقبلمانحالترخص"]}]},
	*{"ClauseName":"GENb","DisplayName":"True","Metadata":"GENb","Description":"GENb","Multilingualdata":[{"language":"English","type":"online",
	*"para":["TheLicensorisnotthedeveloper,ownerorselleroftheUnitoranyotherpartoftheBuildingandmakesnorepresentations,warrantiesorguaranteeswhatsoeverwithrespecttotheUnitoranypartoftheBuilding."]},
	*{"language":"Arabic","type":"online","para":["المرخِّصليسمطورا,مالكأوبائعللوحدةأوأيجزءمنالمبنى,ولايتعهدالمرخِّصبأيةتعهدات,كفالاتأوضماناتمنأينوعفيمايتصلبالوحدةأوأيجزءمنالمبنى."]}]}]}*/
	var savetype=document.getElementById("chClsSaveType").value;
	var ParentClauseName=document.getElementById("parentCls").value;
	var ParentClauseId= document.getElementById("parentClsId").value;
	childJson= getChildJson();
	childArr.push(childJson);
	console.log(JSON.stringify(childArr));
	document.getElementById("childArr").value= JSON.stringify(childArr);
	console.log(document.getElementById("childArr").value);
	var ChildArray=JSON.parse(document.getElementById("childArr").value);
	console.log(ChildArray);		
	
	if(savetype=="edit"){
		saveChildJson["ClauseId"]=document.getElementById("chClauseId").value;
		saveChildJson["Email"]=Email;
		saveChildJson["group"]=group;
		saveChildJson["SFEmail"]=SFEmail;
		saveChildJson["savetype"]=savetype;
		saveChildJson["ParentClauseId"]=ParentClauseId;
		saveChildJson["ParentClauseName"]=ParentClauseName;
		saveChildJson["ClauseName"]=document.getElementById("childClauseName").value;	
		saveChildJson["DisplayName"]="";
		saveChildJson["Metadata"]=document.getElementById("chldMD").value;
		saveChildJson["Description"]=document.getElementById("chldDes").value;
		saveChildJson["Multilingualdata"]=document.getElementById("childMulLangArr").value;
		console.log(JSON.stringify(saveChildJson));
	}else if(savetype=="new"){
		saveChildJson["Email"]=Email;
		saveChildJson["group"]=group;
		saveChildJson["SFEmail"]=SFEmail;
		saveChildJson["savetype"]=savetype;
		saveChildJson["ParentClauseId"]=ParentClauseId;
		saveChildJson["ParentClauseName"]=ParentClauseName;
		saveChildJson["ChildArray"]=ChildArray;		
		console.log(JSON.stringify(saveChildJson));
	}
	
	$.ajax({
		type:'Post',
		url:'/portal/servlet/service/SaveChildClases',
		async: true,
		data:JSON.stringify(saveChildJson),
		contentType:"application/json",
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			getClauseTable();
			/*$('.table-clause-library').show();
			$('.table-clause-library-main').css('display','block');
			$('.create-new-clause-btn').css('display','block');
			$('.parent-clause-main').css('display','none');	*/
		}
	});
	/*$.ajax({
		type:'GET',
		url:'/portal/servlet/service/GetClsByName?email='+Email+'&clauseName='+ParentClauseName,
		async: true,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			console.log(json.clauseId);
			ParentClauseId=json.clauseId;
			
			if(savetype=="edit"){
				saveChildJson["ClauseId"]=document.getElementById("chClauseId").value;
				saveChildJson["Email"]=Email;
				saveChildJson["SFEmail"]=SFEmail;
				saveChildJson["savetype"]=savetype;
				saveChildJson["ParentClauseId"]=ParentClauseId;
				saveChildJson["ParentClauseName"]=ParentClauseName;
				saveChildJson["ClauseName"]=document.getElementById("childClauseName").value;	
				saveChildJson["DisplayName"]="";
				saveChildJson["Metadata"]=document.getElementById("chldMD").value;
				saveChildJson["Description"]=document.getElementById("chldDes").value;
				saveChildJson["Multilingualdata"]=document.getElementById("childMulLangArr").value;
			}else if(savetype=="new"){
				saveChildJson["Email"]=Email;
				saveChildJson["SFEmail"]=SFEmail;
				saveChildJson["savetype"]=savetype;
				saveChildJson["ParentClauseId"]=ParentClauseId;
				saveChildJson["ParentClauseName"]=ParentClauseName;
				saveChildJson["ChildArray"]=ChildArray;					
			}
			
			//alert(JSON.stringify(saveChildJson));
			console.log(JSON.stringify(saveChildJson));			
		},
		complete:function(){
				$.ajax({
				type:'Post',
				url:'/portal/servlet/service/SaveChildClases',
				async: true,
				data:JSON.stringify(saveChildJson),
				contentType:"application/json",
				success:function(dataa){
					//alert(dataa);
					var json=JSON.parse(dataa);
					console.log(json);
					getClauseTable();
					$('.table-clause-library').show();
					$('.table-clause-library-main').css('display','block');
					$('.create-new-clause-btn').css('display','block');
					$('.parent-clause-main').css('display','none');	
				}
			});
		}
	});*/
	
	
	//http://35.221.183.246:8082/portal/servlet/service/DTANewGetClauseList?email=doctiger@xyz.com
	/*$.ajax({
		type:'GET',
		url:'/portal/servlet/service/DTANewGetClauseList?email='+Email,
		async:true,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			var clsArr=[];
			clsArr=json.Clauses;
			console.log(clsArr);
			{"status":"success","Clauses":[{"ClauseId":"1","ClauseName":"sddffdf","Version":"1.1","CreatedBy":"doctiger@xyz.com","ApprovedBy":"Admin",
			*"CreationDate":"MonOct2912:41:54UTC2018","Description":"sddgf","ChildClauses":[{"ChildClauseId":"/content/user/doctiger_xyz.com/DocTigerAdvanced/Clauses/1/ChildClauses/0",
			*"ChildClauseName":"child1","ClauseDescription":"Des1","Version":"1.1","CreatedBy":"doctiger@xyz.com","ApprovedBy":"Admin","CreationDate":"MonOct2912:41:54UTC2018",
			*"ChildClauses":[]},{"ChildClauseId":"/content/user/doctiger_xyz.com/DocTigerAdvanced/Clauses/1/ChildClauses/1","ChildClauseName":"child1",
			*"ClauseDescription":"Des1","Version":"1.1","CreatedBy":"doctiger@xyz.com","ApprovedBy":"Admin","CreationDate":"MonOct2912:41:54UTC2018","ChildClauses":[]}]}]}
			var table=document.getElementById("clauses");
			for(var i=0;i<clsArr.length;i++){
				var clsName=clsArr[i].ClauseName;
				var version=clsArr[i].Version;
				var creDate=clsArr[i].CreationDate;
				var action="<td class='action-btn' style='vertical-align:middle;'><a href='' class=' btn btn-bg-brown btn-sm create-new-clause-btn'><i class='fa fa-pencil'></i></a><a href='' data-href='#' data-toggle='modal' data-target='#confirm-delete' class='btn btn-bg-light-danger btn-sm'><i class='fa fa-trash'></i></a><a href='' class='btn btn-bg-light-blue btn-sm'><i class='fa fa-eye'></i></a><button class='btn btn-bg-light-danger btn-sm open-add-child-div'>Add Child</button></td>";
				console.log(clsName);
				var row=table.insertRow(i+1);
				table.rows[i+1].className="parent-tr";
				var cell1=row.insertCell(0);
				var cell2=row.insertCell(1);
				var cell3=row.insertCell(2);
				var cell4=row.insertCell(3);
				var cell5=row.insertCell(4);
				var cell6=row.insertCell(5);
				var cell7=row.insertCell(6);
				cell1.innerHTML=clsName+"<i class='fa fa-plus btn btn-sm pull-right cfa-open btn-success'></i>";
				cell2.innerHTML="PDF";
				cell3.innerHTML=Email;
				cell4.innerHTML=Email;
				cell5.innerHTML=creDate;
				cell6.innerHTML=version;
				cell7.innerHTML=action;
			}
		}
	});*/
	window.location.reload();
});

var sfTempJson={};
var xJson;
var xret;
$('.mail-template-sfdc-save-next').click(function(){
			$('.select-sfdc-object-main-template').css('display','none');
			if($('#check-external-parameter-temp').is(':checked')){
				$('.external-parameter-main-template').css('display','block');
				$('.compose-clause-main-template').css('display','none');
			}
		else{
				xret=myFunction();
				//alert(x);
				console.log(JSON.parse(xret));
				xret= JSON.parse(xret);
				var cId;
				var cName;
				/*{"ClauseId":"2","ClauseName":"Clause03"}*/
				try{
					for(var i=0;i<xret.length;i++){
						xjson=xret[i];
						console.log(xjson);
						cId= xjson.ClauseId;
						cName= xjson.ClauseName;
						$(".box-left-compose-temp-lib").append('<div class="list-part" id="list'+i+'" li-id="'+xjson+'"><ul><li c-id="'+cId+'">'+cName+'</li></ul></div>');
					}
				}catch(err){
					console.log(err);
				}
				$('.compose-clause-main-template').css('display','block');
				$('.external-parameter-main-template').css('display','none');				
		}
			/*{"templatename":"temp1","email":"doctiger@xyz.com","username":"user@gmail.com","saveType":"new",
			"SFobject":{"Account":["accname","Accno"],"contact":["contactname","contactno"]},"Primerykey":{"object":"Account","key":"Accno"}}*/
});


$("body").on("click",".click-right-compose-temp-lib",function(){
	var liArr=[];
	$("#clsList li.selected").each(function(){
		var liText=$(this).text();
		var cId= $(this).attr("c-id");
		console.log(cId);
		$(".box-right-compose-temp-lib").append('<div class="list-part"><ul id="list"><li c-id="'+cId+'">'+liText+'</li></ul></div>');
		var liJson={};
		liJson["ClauseId"]=cId;
		liJson["ClauseName"]=liText;
		liArr.push(liJson);
		//liArr.push(xret);
			document.getElementById("liArr").value=JSON.stringify(liArr);
			console.log(document.getElementById("liArr").value);
			if($(this).parent("ul").parent("div").find("ul li").length==1){
				$(this).parent("ul").parent("div").remove();
			}else{
				$(this).remove();
			}
	});
});

$("body").on("click",".click-left-compose-temp-lib",function(){
	$("#sel_clsList li.selected").each(function(){
		var liText=$(this).text();
		$(".box-left-compose-temp-lib").append('<div class="list-part"><ul><li>'+liText+'</li></ul></div>');
		if($(this).parent("ul").parent("div").find("ul li").length==1){
			$(this).parent("ul").parent("div").remove();
		}else{
			$(this).remove();
		}
	});
});

$('.temp-external-para-save-next').click(function(){
	/*{"templatename":"temp1","email":"doctiger@xyz.com","saveType":"new","username":"user@gmail.com","externalparamobject":[{"type":"ws","primerykey":"Accno",
	 * "input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string","fieldlength":"255"},{"fieldname":"contactno",
	 * "fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":"amount","fieldtype":"string","fieldlength":"255"},{"fieldname":"invoiceno","fieldtype":"string",
	 * "fieldlength":"255"}],"url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1","password":"pass"}]}*/
	var savetype="new";
	/*var ClsId=document.getElementById("ParentClauseId").value;
	var ClauseId= ClsId;
	*/
	var username= "doctiger8@gmail.com";
	var templatename=document.getElementById("tempName").value;
	var type="";
	var primerykey="";
	exmainJson["email"]=Email;
	exmainJson["group"]=group;
	exmainJson["SFEmail"]=SFEmail;
	exmainJson["saveType"]=savetype;
	exmainJson["username"]=username;
	exmainJson["templatename"]=templatename;
	var externalparamobject=document.getElementById("exparamArr").value;
	console.log(document.getElementById("exparamArr").value);
	if(externalparamobject==null || externalparamobject== ""){
		var url=document.getElementById("web-services-url-temp-lib").value;
		var token=document.getElementById("temp_token").value;
		var username=document.getElementById("temp_user").value;
		var password=document.getElementById("temp_pass").value;
		var webservice= getExparamtempObj(url, token, user, pass);
		exparamArr.push(webservice);
		console.log(exparamArr);
		exmainJson["externalparamobject"]=exparamArr;
		console.log("test");
	}else{
		exmainJson["externalparamobject"]=JSON.parse(document.getElementById("exparamArr").value);		
	}
	
	console.log(JSON.stringify(exmainJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/SaveTemplate.ExternalParameter',
		async: true,
		data:JSON.stringify(exmainJson),
		contentType:"application/json",
		success:function(dataa){
			//alert(dataa);
			console.log(dataa);
			/*var json=JSON.parse(dataa);
			var ClauseId=json.ClauseId;
			document.getElementById("ParentClauseId").value=ClauseId;
			console.log(document.getElementById("ParentClauseId").value);*/
			$('.external-parameter-main-template').css('display','none');
			$('.compose-clause-main-template').css('display','block');
			var x=myFunction();
			//alert(x);
			console.log(x);			
			for(var i=0;i<x.length;i++){
				$(".box-left-compose-temp-lib").append('<div class="list-part"><ul><li>'+x[i].ClauseName+'</li></ul></div>');
				}
		}
	});
});

function myFunction(){
	/*http://35.221.183.246:8082/portal/servlet/service/GetParentClauseList?email=doctiger@xyz.com*/
	var value;
	var ret=[];
	try{
		$.ajax({
			type:'GET',
			url:'/portal/servlet/service/GetParentClauseList?email='+Email+'&group='+group,
			async: false,
			success:function(dataa){
				//alert(dataa);
				var json=JSON.parse(dataa);
				console.log(json);
				var ParentClause=json.ParentClause;
				console.log(ParentClause);
				var arr=[];
				var lstArr=[];
				for(var i=0;i<ParentClause.length;i++){
					var ClauseName=ParentClause[i].ClauseName;
					var clsJson=ParentClause[i];
					console.log(ClauseName);
					console.log(clsJson);
					arr.push(ClauseName);
					lstArr.push(clsJson);
				}
				document.getElementById("lstArr").value=JSON.stringify(lstArr);
				ret= document.getElementById("lstArr").value;
				console.log(document.getElementById("lstArr").value);
				console.log(ret);
				$('#clsarr').val(JSON.stringify(arr));//storearray
				console.log(JSON.stringify(arr));
				$('#lstArr').val(JSON.stringify(lstArr));
				value=$('#clsarr').val();//retrievearray
				value=JSON.parse(value);
				console.log(value);
			}
		});
		/*clsarr=$('#clsarr').val();//retrievearray
		clsarr=JSON.parse(clsarr);
		console.log(JSON.parse(clsarr));*/
	}catch(e){
		console.log("Error:"+e.description);
	}
	//return value;
	console.log(ret);
	return ret;
}
			
$('#dynamic').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-dynamic-main-temp-lib').css('display','block');
	}else{
		$('.select-dynamic-main-temp-lib').css('display','none');
	}
});

$('#workflow-template').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-workflow-main-temp').css('display','block');
	}else{
		$('.select-workflow-main-temp').css('display','none');
	}
});

/*$('.compose-clause-temp-lib-save-btn').click(function(){
	$('.createNewTempMain').css('display','none');
	$('.select-sfdc-object-main-template').css('display','none');
	$('.external-parameter-main-template').css('display','none');
	$('.compose-clause-main-template').css('display','none');
	$('.table-clause-template').show();
	$('.table-clause-template-main').css('display','block');
});			*/

var composeTempJson={};
$('.compose-clause-temp-lib-save-next-btn').click(function(){
//http://35.221.183.246:8082/portal/servlet/service/SaveTemplate.Compose
/*{"templatename":"temp1","email":"doctiger@xyz.com","username":"user@gmail.com","NumStyle":"","selectedclause":[{"ClauseId":"0","ClauseName":"C1"},{"ClauseId":"1","ClauseName":"C2"}],
"uploadtemplate":{"filename":"abc.docx","filedata":"RGVhciBTaXJzLA0KV2UgaGVyZWJ5IGdpdmUgeW91IG5vdGljZSBvZiBvdXIgaW50ZW50aW9ucyB0byBsZWF2ZSANCiB0aGUgTWFuYWdlZCBSZW50YWwgPDxBY2NvdW50X05hbWU+Pg0K"},"advanced":"true",
"advancedobject":{"selectedrule":"rule1","selectedworkflow":"workflow1","selecteddocumentworkflow":"workflow1"}}*/
var templatename=document.getElementById("tempName").value;
var username="user@gmail.com";
var NumStyle=document.getElementById("numStyle").value;
var selectedclause=JSON.parse(document.getElementById("liArr").value);
var uploadtemplate={};
var advanced;
if($('#checkAdvancedComposeTempLib').is(":checked")){
	advanced="true";
}else{
	advanced="false";
}



//if(document.getElementById("file-upload").value !="")
//{
var reader = new FileReader();

var fname = document.getElementById("file-upload").value;
// console.log("filename-- " + fname);
var f = document.getElementById("file-upload").files;
reader.readAsDataURL(f[0]);

reader.onloadend = function() {
	console.log(advanced);
	var advancedobject={};
	var selectedrule=document.getElementById("RuleEngine").value;
	var selectedworkflow=document.getElementById("wf_sel").value;
	var selecteddocumentworkflow=document.getElementById("wfdoc_sel").value;
	var filename=document.getElementById("upfile").value;
	//var filedata=document.getElementById("base64docx").value;
	uploadtemplate["filename"]=filename;
	//uploadtemplate["filedata"]=filedata;
	advancedobject["selectedrule"]=selectedrule;
	advancedobject["selectedworkflow"]=selectedworkflow;
	advancedobject["selecteddocumentworkflow"]=selecteddocumentworkflow;
	composeTempJson["templatename"]=templatename;
	composeTempJson["email"]=Email;
	composeTempJson["group"]=group;
	composeTempJson["username"]=username;
	composeTempJson["NumStyle"]=NumStyle;
	composeTempJson["selectedclause"]=selectedclause;
	composeTempJson["uploadtemplate"]=uploadtemplate;
	composeTempJson["advanced"]=advanced;
	composeTempJson["advancedobject"]=advancedobject;
	//alert(JSON.stringify(composeTempJson));
	var filedata = reader.result;

	var fd = filedata.substr(0, filedata.indexOf(",") + 1);
	var fdata = filedata.replace(fd, "");
//	 console.log("filedata-- " + fdata);
	uploadtemplate["filedata"]=fdata;
	
	// console.log("json file- " + JSON.stringify(filejs));

	// new

	

console.log(JSON.stringify(composeTempJson));
$.ajax({
	type:'POST',
	url:'/portal/servlet/service/SaveTemplate.Compose',
	async: true,
	data:JSON.stringify(composeTempJson),
	contentType:"application/json",
	success:function(dataa){
		//alert(dataa);
		console.log(dataa);
		alert("Template saved successfully");
		//var json=JSON.parse(dataa);
		
		/*$('.createNewTempMain').css('display','none');
		$('.create-new-template-btn').css('display','block');
		$('.select-sfdc-object-main-template').css('display','none');
		$('.external-parameter-main-template').css('display','none');
		$('.compose-clause-main-template').css('display','none');
		*/
		
		getTemplateTable();
		window.location.reload();
		$('.compose-clause-main-template').css('display','none');
		$('.table-clause-template').show();
		$('.table-clause-template-main').css('display','block');
	
	}
});
}
});

var mailtempsf={};
$('.mail-temp-sfdc-save-next').click(function(){
	var mailSmsTemp=$('input[name=mail-sms-temp]:checked').val();
	$('.select-sfdc-object-mail-temp-main').css('display','none');
	if($('#external-parameter-mail-temp').is(':checked')){
		$('.external-parameter-mail-temp-main').css('display','block');
		$('.compose-clause-mail-temp-main').css('display','none');
	}else{
		if(mailSmsTemp=='mail'){
			$('.select-sfdc-object-main-template').css('display','none');
			if($('#check-external-parameter-temp').is(':checked')){
				$('.external-parameter-main-template').css('display','block');
				$('.compose-clause-main-template').css('display','none');
			}else{
				$('.compose-clause-main-template').css('display','block');
				$('.external-parameter-main-template').css('display','none');
			}
			/*{"mailtemplatename":"Mailtemp","email":"doctiger@xyz.com","username":"user@gmail.com","saveType":"new",
"SFObject":{"Add":["accname","Accno"],"cdd":["contactname","contactno"]},"Primerykey":{"Object":"Account","key":"Accno"}}*/
			/*varsaveType="new";
			varusername="doctiger@xyz.com";
			varmailtemplatename=document.getElementById("mailTempName").value;
			varjsonsfobj=JSON.parse(document.getElementById("SFObject").value);
			varsfobj=jsonsfobj.SFobject;
			varsel_SFFields=document.getElementById("sel_SFFieldsMail").value;
			console.log(document.getElementById("sel_SFFieldsMail").value);
			varpri_key=JSON.parse(document.getElementById("Pri_Key").value);
			console.log(sfobj);
			mailtempsf["email"]=Email;
			mailtempsf["saveType"]=saveType;
			mailtempsf["username"]=username;
			mailtempsf["templatename"]=templatename;
			mailtempsf["Primerykey"]=pri_key;
			mailtempsf["SFobject"]=sfobj;
			//alert(JSON.stringify(mailtempsf));
			console.log(JSON.stringify(mailtempsf));
			$.ajax({
					type:'POST',
					url:'/portal/servlet/service/SaveTemplate.SFObj',
					async:true,
					data:JSON.stringify(mailtempsf),
					contentType:"application/json",
					success:function(sfdata){
						//alert(sfdata);
						var json=JSON.parse(sfdata);
						var tempName=json.templatename;
						document.getElementById("tempName").value=tempName;
						console.log(document.getElementById("tempName").value);
						var x=myFunction();
						//alert(x);
						console.log(x);
						for(var i=0;i<x.length;i++){
							$(".box-left-compose-temp-lib").append('<divclass="list-part"><ul><li>'+x[i]+'</li></ul></div>');
							}
						}
					});
*/			
			$('.compose-clause-mail-temp-main').css('display','block');
			$('.compose-clause-sms-temp-main').css('display','none');
		}
		if(mailSmsTemp=='sms'){
			$('.compose-clause-mail-temp-main').css('display','none');
			$('.compose-clause-sms-temp-main').css('display','block');
		}
		$('.external-parameter-mail-temp-main').css('display','none');
	}
});

var sfObj={};
function getSFObj(heading,liText){
	var sf=[];
	if(sfObj.hasOwnProperty(heading)){
		console.log(heading);
		sfObj[heading].push(liText);
		sfmainJson["SFobject"]=sfObj;
	}else{
		console.log(heading);
		sf.push(liText);
		sfObj[heading]=sf;
		sfmainJson["SFobject"]=sfObj;
	}
	console.log(sf);
	console.log(sfObj);
	console.log(JSON.stringify(sfmainJson));
	document.getElementById("SFObject").value=JSON.stringify(sfmainJson);
	console.log(document.getElementById("SFObject").value);
	
}

$('.mail-temp-external-save-next').click(function(){
	//$('.external-parameter-mail-temp-main').css('display','none');
	var mailSmsTemp=$('input[name=mail-sms-temp]:checked').val();
	/*{"email":"doctiger@xyz.com","saveType":"new","mailtemplatename":"Mail temp","externalparamobject":[{"type":"ws","primerykey":"Accno",
	 *  "input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string","fieldlength":"255"}],
	 *  "output":[{"fieldname":"amount","fieldtype":"string","fieldlength":"255"},{"fieldname":"invoiceno","fieldtype":"string","fieldlength":"255"}],
	"url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1","password":"pass"}]}*/
	var savetype="new";
	var mailTempName=document.getElementById("mailTempName").value;
	var smsTempName= document.getElementById("smsTempName").value;
	var type="";
	var primerykey="";
		
	exmainJson["email"]=Email;
	exmainJson["group"]=group;
	exmainJson["saveType"]=savetype;
	var externalparamobject=document.getElementById("exparamArr").value;
	console.log(document.getElementById("exparamArr").value);
	if(externalparamobject==null || externalparamobject== ""){
		var url=document.getElementById("web-services-url-mail-temp").value;
		var token=document.getElementById("mailtemp_token").value;
		var username=document.getElementById("mailtemp_user").value;
		console.log(username);
		var password=document.getElementById("mailtemp_pass").value;
		var webservice= getExparamMailtempObj(url, token, user, pass);
		exparamArr.push(webservice);
		console.log(exparamArr);
		exmainJson["externalparamobject"]=exparamArr;
		console.log("test");
	}else{
		exmainJson["externalparamobject"]=JSON.parse(document.getElementById("exparamArr").value);		
	}
	
	if(mailSmsTemp=='mail'){
		exmainJson["mailtemplatename"]=mailTempName;
		console.log(exmainJson);
		$.ajax({
			type:'POST',
			url:'/portal/servlet/service/DTAMailTemp.exparam',
			async: true,
			data:JSON.stringify(exmainJson),
			contentType:"application/json",
			success:function(dataa){
				//alert(dataa);
				console.log(dataa);
				alert("MailTemplate saved successfully");
				$('.external-parameter-mail-temp-main').css('display','none');
				$('.compose-clause-mail-temp-main').css('display','block');
				$('.compose-clause-sms-temp-main').css('display','none');
			}
		});
	}else if(mailSmsTemp=='sms'){
		exmainJson["smstemplatename"]=smsTempName;
		console.log(exmainJson);
		$.ajax({
			type:'POST',
			url:'/portal/servlet/service/SaveSMSTemplate.ExternalParameter',
			async: true,
			data:JSON.stringify(exmainJson),
			contentType:"application/json",
			success:function(dataa){
				//alert(dataa);
				console.log(dataa);
				$('.external-parameter-mail-temp-main').css('display','none');
				$('.compose-clause-mail-temp-main').css('display','none');
				$('.compose-clause-sms-temp-main').css('display','block');
			}		
		});		
	}				
});

$('#rules-based-mail-temp').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-rule-mail-temp').css('display','block');
	}else{
		$('.select-rule-mail-temp').css('display','none');
	}
});

$('#workflow-template-mail').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-workflow-mail-temp').css('display','block');
	}else{
		$('.select-workflow-mail-temp').css('display','none');
	}
});

$('.mail-temp-compose-save-btn').click(function(){
	$('.createNewMailTempMain').css('display','none');
	$('.select-sfdc-object-mail-temp-main').css('display','none');
	$('.external-parameter-mail-temp-main').css('display','none');
	$('.compose-clause-mail-temp-main').css('display','none');
	$('.table-mail-template').show();
	$('.table-mail-template-main').css('display','block');
});

var composeMailJson={};
$('.mail-temp-compose-save-next-btn').click(function(){
	
/*{"mailtemplatename":"Mail temp","email":"doctiger@xyz.com","username":"doctiger@xyz.com", "saveType":"new","from":"fromid","to":"toid","subject":
* "subjet string","body":"body skjfhhgf","filename":"abc.docx","filedata":"UEsDBBQABgAIAAAAIQDwIex9jgEAABMGAAATAAgCW0NvbnRlbnRfVHlwZXNdLnhtbCCiBAIooAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
* AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
* AAAAAAAAAAAAAAAAAAA","advanced":"true","selectedrule":"rule1", "Ruledata":{"URL":"http://35.236.213.87:8080/drools/callrules/doctiger@xyz.com_doctiger_project_rulesdoctiger/fire","Input":[],"Output":[]},
* "selectedworkflow":"workflow1"} 
*/
/*{"smstemplatename":"SMS1temp","email":"doctiger@xyz.com",  "SFEmail":"doctiger@gmail.com",
 * "username":"user@gmail.com", "from":"abcgmail","to":"dd","body":"ss"}*/
	var mailSmsTemp=$('input[name=mail-sms-temp]:checked').val();
	
	var savetype="new";
	var username="doctiger@xyz.com";
	
	composeMailJson["email"]=Email;
	composeMailJson["group"]=group;
	composeMailJson["savetype"]=savetype;
	composeMailJson["username"]=username;
	
	if(mailSmsTemp=="mail"){
		var mailtemplatename=document.getElementById("mailTempName").value;
		var from= document.getElementById("from").value;
		var to= document.getElementById("to").value;
		var subject= document.getElementById("subject").value;
		var body= CKEDITOR.instances['body'].getData();
		var filename= "";
		var filedata="";
		var advanced;
		if($('#checkAdvancedComposeMailTemp').is(":checked")){
			advanced="true";
		}else{
			advanced="false";
		}
		var advancedobject={};
		var uploadtemplate={};
		var selectedrule=document.getElementById("RuleEngine").value;
		var selectedworkflow=document.getElementById("wf_sel").value;
		var filename=document.getElementById("upfiledoc").value;
		var filedata=document.getElementById("base64mailtemp").value;
		uploadtemplate["filename"]=filename;
		uploadtemplate["filedata"]=filedata;
		var Ruledata= document.getElementById("RuleData").value;
		composeMailJson["mailtemplatename"]=mailtemplatename;
		composeMailJson["from"]=from;
		composeMailJson["to"]=to;
		composeMailJson["subject"]=subject;
		composeMailJson["body"]=body;
		composeMailJson["filename"]=filename;
		composeMailJson["filedata"]=filedata;
		composeMailJson["advanced"]=advanced;
		composeMailJson["selectedrule"]=selectedrule;
		composeMailJson["Ruledata"]=Ruledata;
		composeMailJson["selectedworkflow"]=selectedworkflow;

		console.log(JSON.stringify(composeMailJson));
		$.ajax({
			type:'POST',
			url:'/portal/servlet/service/DTAMailTemp.compose',
			async: true,
			data:JSON.stringify(composeMailJson),
			contentType:"application/json",
			success:function(dataa){
				//alert(dataa);
				console.log(dataa);
				
				getMailTempTable();		
				window.location.reload();
			}
		});	
	}else if(mailSmsTemp=="sms"){
		var smstemplatename=document.getElementById("smsTempName").value;
		var from= document.getElementById("from_no").value;
		var to= document.getElementById("to_no").value;
		var body= CKEDITOR.instances['sms_body'].getData();
		composeMailJson["smstemplatename"]=smstemplatename;
		composeMailJson["from"]=from;
		composeMailJson["to"]=to;
		composeMailJson["body"]=body;
		
		console.log(JSON.stringify(composeMailJson));
		$.ajax({
			type:'POST',
			url:'/portal/servlet/service/SaveSMSTemplate.Compose',
			async: true,
			data:JSON.stringify(composeMailJson),
			contentType:"application/json",
			success:function(dataa){
				//alert(dataa);
				console.log(dataa);
				
				getMailTempTable();		
				window.location.reload();
			}
		});	
	}
	});

$("body").on("change",".change-attach",function(){
	var option=$(this).val();
	if(option=="SelectController"){
		$(this).parents('.comunication-sectioin-last').find('.controller-textbox').css('display','block');
		$(this).parents('.comunication-sectioin-last').find('.file-path-textbox').css('display','none');
	}else if(option=="SelectFilePath"){
		$(this).parents('.comunication-sectioin-last').find('.file-path-textbox').css('display','block');
		$(this).parents('.comunication-sectioin-last').find('.controller-textbox').css('display','none');
	}else if(option=="SelectAttachment"||option=="Template1"||option=="Template2"){
		$(this).parents('.comunication-sectioin-last').find('.file-path-textbox').css('display','none');
		$(this).parents('.comunication-sectioin-last').find('.controller-textbox').css('display','none');
	}
});
var items={'Account':['AccountId','AccountName'],'Case':['CaseId','CaseSubject'],'Lead':['LeadId','LeadName'],'Oppurtunity':['OppurtunityId','OppurtunityName']};
			//Clauselibrarytabsf-object
$("body").on("change", ".sf-object", function () {
	var heading = $(this).val();
	console.log(heading);
	if ($(".box-left").find('h3[data-name="' + heading + '"]').text() == '') {
		$(".box-left").append('<div class="list-part"><h3 data-name="' + heading + '">' + heading + '</h3><ul><li>' + items[heading]['0'] + '</li><li>' + items[heading]['1'] + '</li></ul></div>');
	}
	console.log(items[heading]);
});

/*$("body").on("change",".sf-object",function(){
	var heading=$(this).val();
	console.log(heading);
	if($(".box-left").find('h3[data-name="'+heading+'"]').text()==''){
		$(".box-left").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+items[heading]['0']+'</li><li>'+items[heading]['1']+'</li></ul></div>');
	}
	console.log(items[heading]);
});	*/

$("body").on("click",".click-right",function(){
	$(".boxli.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		console.log("heading:"+heading);
		getSFObj(heading,liText);
		/*varsf=[];
		if(sfObj.hasOwnProperty(heading)){
			console.log(heading);
			sfObj[heading].push(liText);
			sfmainJson["SFobject"]=sfObj;
		}else{
			console.log(heading);
			sf.push(liText);
			sfObj[heading]=sf;
			sfmainJson["SFobject"]=sfObj;
		}
		console.log(sf);
		console.log(sfObj);
		console.log(JSON.stringify(sfmainJson));
		document.getElementById("SFObject").value=JSON.stringify(sfmainJson);
		console.log(document.getElementById("SFObject").value);
		*/
		if($(".box-right").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-right").find('h3[data-name="'+heading+'"]').parent("div").find("ul").append("<li>"+liText+"</li>");
		}else{
			$(".box-right").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+liText+'</li></ul></div>');
				}
				if($(this).parent("ul").parent("div").find("ul li").length==1){
					$(this).parent("ul").parent("div").remove();
				}else{
					$(this).remove();
				}
			});
	});

$("body").on("click",".click-left",function(){
	$(".box li.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		if($(".box-left").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-left").find('h3[data-name="'+heading+'"]').parent("div").find("ul").append("<li>"+liText+"</li>");
		}
		else{
			$(".box-left").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+liText+'</li></ul></div>');
		}
		if($(this).parent("ul").parent("div").find("ul li").length==1){
			$(this).parent("ul").parent("div").remove();
		}else{
			$(this).remove();
		}
	});
});		

//Templatelibrarytabsf-object
$("body").on("change",".sf-object-temp-lib",function(){
	var heading=$(this).val();
	if($(".box-left-temp-lib").find('h3[data-name="'+heading+'"]').text()==''){
		$(".box-left-temp-lib").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+items[heading]['0']+'</li><li>'+items[heading]['1']+'</li></ul></div>');
	}
	console.log(items[heading]);
});

//System>mailtemplatetabsf-object
$("body").on("change",".sf-object-mail-temp",function(){
	//alert("test");
	var heading=$(this).val();
	console.log(heading);
	if($(".box-left-mail-temp").find('h3[data-name="'+heading+'"]').text()==''){
		$(".box-left-mail-temp").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+items[heading]['0']+'</li><li>'+items[heading]['1']+'</li></ul></div>');															
	}
	console.log(items[heading]);
});	

$("body").on("click",".click-right-temp-lib",function(){
	$(".box li.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		console.log("heading:"+heading);
		getSFObj(heading,liText);
		/*varsf=[];
		if(sfObj.hasOwnProperty(heading)){
			console.log(heading);
			sfObj[heading].push(liText);
			sfmainJson["SFobject"]=sfObj;
		}else{
			console.log(heading);
			sf.push(liText);
			sfObj[heading]=sf;
			sfmainJson["SFobject"]=sfObj;
		}
		console.log(sf);
		console.log(sfObj);
		console.log(JSON.stringify(sfmainJson));
		document.getElementById("SFObject").value=JSON.stringify(sfmainJson);
		console.log(document.getElementById("SFObject").value);
		*/
		if($(".box-right-temp-lib").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-right-temp-lib").find('h3[data-name="'+heading+'"]').parent("div").find("ul").append("<li>"+liText+"</li>");
		}
		else{
			$(".box-right-temp-lib").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3></div>');
		}
		if($(this).parent("ul").parent("div").find("ul li").length==1){
			$(this).parent("ul").parent("div").remove();
		}else{
			$(this).remove();
		}
	});
});

$("body").on("click",".click-left-temp-lib",function(){
	$(".box li.selected").each(function(){
	var heading=$(this).parent("ul").parent("div").find("h3").text();
	var liText=$(this).text();
	if($(".box-left-temp-lib").find('h3[data-name="'+heading+'"]').text()!=''){
		$(".box-left-temp-lib").find('h3[data-name="'+heading+'"]').parent("div").find("ul").append("<li>"+liText+"</li>");
	}
	else{
		$(".box-left-temp-lib").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+liText+'</li></ul></div>');
	}
	if($(this).parent("ul").parent("div").find("ul li").length==1){
		$(this).parent("ul").parent("div").remove();
	}else{
		$(this).remove();
	}
	});
});

$("body").on("click",".click-right-mail-temp",function(){
	$(".box li.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		getSFObj(heading,liText);
		if($(".box-right-mail-temp").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-right-mail-temp").find('h3[data-name="'+heading+'"]').parent("div").find("ul").append("<li>"+liText+"</li>");
			}
		else{
			$(".box-right-mail-temp").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul><li>'+liText+'</li></ul></div>');
		}
		if($(this).parent("ul").parent("div").find("ul li").length==1){
			$(this).parent("ul").parent("div").remove();
		}else{
			$(this).remove();
		}
	});
});

$("body").on("click",".click-left-mail-temp",function(){
	$(".box li.selected").each(function(){
		var heading=$(this).parent("ul").parent("div").find("h3").text();
		var liText=$(this).text();
		if($(".box-left-mail-temp").find('h3[data-name="'+heading+'"]').text()!=''){
			$(".box-left-mail-temp").find('h3[data-name="'+heading+'"]').parent("div").find("ul").append("<li>"+liText+"</li>");
			}
			else{
				$(".box-left-mail-temp").append('<div class="list-part"><h3 data-name="'+heading+'">'+heading+'</h3><ul>li>'+liText+'</li></ul></div>');
				}
				if($(this).parent("ul").parent("div").find("ul li").length==1){
					$(this).parent("ul").parent("div").remove();
				}else{
					$(this).remove();
				}
			});
	});

$("body").on("click",".box li",function(){
	$(this).toggleClass('selected');
	console.log($(this));
});

var input=document.getElementById('file-upload');
var infoArea=document.getElementById('file-upload-filename');
input.addEventListener('change',showFileName);

function showFileName(event){
	var input=event.srcElement;
	var fileName=input.files[0].name;
	infoArea.textContent=fileName;
	console.log(fileName);
	document.getElementById("upfile").value=fileName;
	var reader=new FileReader();
	var f=document.getElementById("file-upload").files;
	reader.onloadend=function(){
		console.log(reader.result);
		document.getElementById("base64docx").value=reader.result;
		//console.log(document.getElementById("base64doc").value);
	}
	reader.readAsDataURL(f[0]);
}

var uploadExcel={};
var Moduletype="";
$("body").on("change",".test-xls",function(){
	
	if($(this).attr('id')=="upload-xls"){
		Moduletype= "clause";
	}
	else if($(this).attr('id')=="upload-xls-temp-lib"){
		Moduletype= "template";
	}else if($(this).attr('id')=="upload-xls-mail-temp") {
		Moduletype= "mailtemplate";
	}
	var file_id= $(this).attr('id');
	var fileName=document.getElementById(file_id).value;
	console.log(fileName);
	console.log($(this).attr('id'));
	excelFileUpload(file_id, Moduletype, fileName);
	
	});																		

$("body").on("change",".test-doc",function(){
	
	if($(this).attr('id')=="upload-doc"){
		Moduletype= "clause";
	}
	/*else if($(this).attr('id')=="upload-xls-temp-lib"){
		Moduletype= "template";
	}else{
		console.log("write here for mailtemp");
	}*/
	var file_id= $(this).attr('id');
	var fileName=document.getElementById(file_id).value;
	console.log(fileName);
	console.log($(this).attr('id'));
	//excelFileUpload(file_id, Moduletype, fileName);
	var ip10=document.getElementById('upload-doc');
	var infoArea10=document.getElementById('upload-doc-filename');
	//input7.addEventListener('change',showFileName7);
	//function showFileName10(event){
		//var ip10=event.srcElement;
		var fileName10=ip10.files[0].name;
		infoArea10.textContent=fileName;
		console.log(infoArea10);
	//}
	getFileNamenData(file_id, fileName);
	});		

function getFileNamenData(fileId, fileName){
	var cleanName=fileName.split('\\').pop();
	$(this).parent("div").find(".test-doc-filename").text(cleanName);
	console.log(fileName);
	console.log(cleanName);
	console.log(document.getElementById(fileId).value);	
	var reader=new FileReader();
	
	var f=document.getElementById(fileId).files;
	reader.readAsDataURL(f[0]);
	console.log(f);
	reader.onloadend=function(){
		console.log(reader.result);
		document.getElementById("base64").value=reader.result;
		console.log(document.getElementById("base64").value);
		var savetype="new";
		var filename=cleanName;
		var filedata=reader.result;
		
		var fd= filedata.substr(0, filedata.indexOf(",")+1);
		var fdata= filedata.replace(fd, "");
	
		document.getElementById("docFileName").value=filename;
		document.getElementById("docFileData").value=fdata;
		console.log(document.getElementById("docFileName").value);
		console.log(document.getElementById("docFileData").value);
}
}

function excelFileUpload(fileId, Moduletype, fileName){	
	var cleanName=fileName.split('\\').pop();
	$(this).parent("div").find(".test-xls-filename").text(cleanName);
	console.log(fileName);
	console.log(cleanName);
	console.log(document.getElementById(fileId).value);
	/*{"filename":"demo.xls","filedata":"0M8R4KGxGuEAAAAAAAAAAAAAAAAAAAAAPgADAP7/CQAGAAAAAAAAAAAAAAABAAAAKwAAAAAAAAAAEAAA/v///wAAAAD+////AAAAACoAAAD//////////////////////////////////////////////",
	*"Moduletype":"clause","clauseid":"24","clauseName":"C12","Email":"doctiger@xyz.com","SFEmail":"user1@gmail.com"}*/
	var reader=new FileReader();
	
	var f=document.getElementById(fileId).files;
	reader.readAsDataURL(f[0]);
	console.log(f);
	reader.onloadend=function(){
		console.log(reader.result);
		document.getElementById("base64").value=reader.result;
		console.log(document.getElementById("base64").value);
		var savetype="new";
		var filename=cleanName;
		var filedata=reader.result;
		
		var fd= filedata.substr(0, filedata.indexOf(",")+1);
		var fdata= filedata.replace(fd, "");
		var ClsId="";
		var ClauseId="";
		var ClauseName="";
		var Id_name1="";
		if(Moduletype=="clause"){
			 ClsId=document.getElementById("ParentClauseId").value;
			 ClauseId=ClsId;
			 ClauseName=document.getElementById("cn").value;
		}else if(Moduletype=="template"){
			ClauseName=document.getElementById("tempName").value;
		}else if(Moduletype=="mailtemplate"){
			ClauseName=document.getElementById("tempName").value;
			 Id_name1 = $('.temp-name').attr('id');
			
			if(Id_name1=="mailName"){
				 ClauseName=document.getElementById("mailName").value;
			}
			else if(Id_name1=="smsName"){
				 ClauseName=document.getElementById("smsName").value;
			}
		}
		console.log("ClauseName  "+ClauseName)
			uploadExcel["Email"]=Email;
			uploadExcel["group"]=group;
			uploadExcel["SFEmail"]=SFEmail;
			uploadExcel["savetype"]=savetype;
			uploadExcel["ClauseId"]=ClauseId;
			uploadExcel["ClauseName"]=ClauseName;
			uploadExcel["filename"]=filename;
			uploadExcel["filedata"]=fdata;
			uploadExcel["Moduletype"]=Moduletype;
			uploadExcel["Id_name1"]=Id_name1;

			console.log(uploadExcel);
			$.ajax({
				type:'POST',
				url:'/portal/servlet/service/uploadcheckfile_newpath_2',
				async:true,
				data:JSON.stringify(uploadExcel),
				//datatype:"json",
				contentType:"application/json",
				success:function(dataa){
					console.log(dataa);
				}
			});
		}
}

var exparamArr=[];
$('.web-services-add').click(function(){
	$('.web-services').toggleClass('display-block','display-none');
	/*var i=0;
	 * var original=document.getElementById('ws');
	 * var clone=original.cloneNode(true);//"deep"clone
	 * clone.id="ws"+++i;//therecanonlybeoneelementwithanID
	 * original.parentNode.appendChild(clone);	*/
	var webUrl=document.getElementById("web-services-url").value;
	var uploadXls=document.getElementById("upload-xls").files.length;
	console.log(webUrl);	
	//document.getElementById('ws').innerHTML="";
	if(webUrl!=''){
		var a='';
		a=a+'<div class="row form-group">';
		a=a+'<div class="col-sm-3">';
		a=a+'<input type="text" class="form-control" value="'+webUrl+'"placeholder="URLName">';
		a=a+'</div>';
		a=a+'<div class="col-sm-3">';
		a=a+'<button class="btn btn-warnin gsm-btn-custom"><i class="fa fa-edit"></i></button>';
		a=a+'<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
		a=a+'</';
		a=a+'</div>';
		$('.add-url-name-main').append(a);
		$('.web-servicesinput').val('');
		$('.web-servicesselect').val('');
		var webservice= getExparamObject("web-services-url","token","user","pass");
		exparamArr.push(webservice);
		console.log(exparamArr);
		document.getElementById("exparamArr").value=JSON.stringify(exparamArr);
		console.log(document.getElementById("exparamArr").value);
	}
	if(uploadXls!='0'){
		vara='';
		a=a+'<div class="row form-group">';
		a=a+'<div class="col-sm-3">';
		a=a+'<input type="text" class="form-control" value="'+webUrl+'" placeholder="URLName">';
		a=a+'</div>';
		a=a+'<div class="col-sm-3">';
		a=a+'<button class="btn btn-warning sm-btn-custom"><i class="fa fa-edit"></i></button>';
		a=a+'<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
		a=a+'</div>';
		a=a+'</div>';
		$('.add-url-name-main').append(a);
		$('.web-servicesinput').val('');
		$('.web-servicesselect').val('');
		$('.web-servicesselect').val('');
		document.getElementById("upload-xls").value="";
		$("#upload-xls-filename").text('FileName');
	}
});

var inputArr=[];
var outputArr=[];
var count=0;
function getExparamObject(url, token, user, pass){
	var ipcls= document.getElementsByClassName("ipfld");
	var opcls= document.getElementsByClassName("opfld");
	for(var i=0; i<ipcls.length; i++){
		var Id=document.getElementsByClassName('ipfld')[i].id;//0-prev val
		console.log(Id);
		if(Id=="ip" || Id=="ip"+i){
			console.log(count);
			count++;
			if(count==1){
				if(Id=="ip"){
					var fieldname=document.getElementById("ip_fieldName").value;
					var fieldtype=document.getElementById("ip_fieldType").value;
					var fieldlength=parseInt(document.getElementById("ip_fieldLength").value);				
				}
				var ip_row={};
				ip_row["fieldname"]=fieldname;
				ip_row["fieldtype"]=fieldtype;
				ip_row["fieldlength"]=fieldlength;
				inputArr.push(ip_row);
			}else{
				if(Id=="ip"+i){
					var fieldname=document.getElementById("ip_fieldNameCopy").value;
					var fieldtype=document.getElementById("ip_fieldTypeCopy").value;
					var fieldlength=parseInt(document.getElementById("ip_fieldLengthCopy").value);
				}			
				var ip_row={};
				ip_row["fieldname"]=fieldname;
				ip_row["fieldtype"]=fieldtype;
				ip_row["fieldlength"]=fieldlength;
				inputArr.push(ip_row);
			}
			document.getElementById("ipArr").value=JSON.stringify(inputArr);
			console.log(inputArr);
			console.log(document.getElementById("ipArr").value);
		}			
	}
	count=0;
	for(var i=0; i<opcls.length; i++){
		var Id=document.getElementsByClassName('opfld')[i].id;
		console.log(Id);		
		if(Id=="op" || Id=="op"+i){
			console.log(count);
			count++;
			console.log(count);
			if(count==1){
				if(Id=="op"){
					var fieldname=document.getElementById("op_fieldName").value;
					var fieldtype=document.getElementById("op_fieldType").value;
					var fieldlength=parseInt(document.getElementById("op_fieldLength").value);								
				}
				var op_row={};
				op_row["fieldname"]=fieldname;
				op_row["fieldtype"]=fieldtype;
				op_row["fieldlength"]=fieldlength;
				outputArr.push(op_row);
			}else{
				if(Id=="op"+i){
					var fieldname=document.getElementById("op_fieldNameCopy").value;
					var fieldtype=document.getElementById("op_fieldTypeCopy").value;
					var fieldlength=parseInt(document.getElementById("op_fieldLengthCopy").value);	
					console.log(fieldname+" "+fieldtype+" "+fieldlength);
				}var op_row={};
				op_row["fieldname"]=fieldname;
				op_row["fieldtype"]=fieldtype;
				op_row["fieldlength"]=fieldlength;
				outputArr.push(op_row);
			}
			//outputArr.push(op_row);
			document.getElementById("opArr").value=JSON.stringify(outputArr);
			console.log(outputArr);
			console.log(document.getElementById("opArr").value);
		}
	}
	var ip=JSON.parse(document.getElementById("ipArr").value);
	var op=JSON.parse(document.getElementById("opArr").value);
	var primerykey=document.getElementById("prikey").value;
	/*{"Email":"doctiger@xyz.com","SFEmail":"user1@gmail.com","savetype":"new","ClauseId":"24","ClauseName":"C12","url":"http://json.parser.online.fr/",
	"externalparamobject":[{"type":"ws","primerykey":"Accno","input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"}],
	"output":[{"fieldname":"amount","fieldtype":"string","fieldlength":"255"}],"token":"yteywetryery","username":"u1","password":"pass"}]}*/
	var webservice={};
	webservice["type"]="ws";
	webservice["primerykey"]=primerykey;
	webservice["input"]=ip;
	webservice["output"]=op;
	webservice["url"]=url;
	webservice["token"]=token;
	webservice["username"]=user;
	webservice["password"]=pass;
	
	return webservice;
}

function getExparamtempObj(url, token, user, pass){
	var tempip= document.getElementsByClassName("tempipfld");
	var tempop= document.getElementsByClassName("tempopfld");
	for(var i=0; i<tempip.length; i++){
		var Id=document.getElementsByClassName('tempipfld')[i].id;//0-prev val
		console.log(Id);
		if(Id=="temp_ip" || Id=="temp_ip"+i){
			console.log(count);
			count++;
			if(count==1){
				if(Id=="temp_ip"){
					var fieldname=document.getElementById("ip_tempFN").value;
					var fieldtype=document.getElementById("ip_tempFT").value;
					var fieldlength=parseInt(document.getElementById("ip_tempFL").value);	
				}
				var ip_row={};
				ip_row["fieldname"]=fieldname;
				ip_row["fieldtype"]=fieldtype;
				ip_row["fieldlength"]=fieldlength;
				inputArr.push(ip_row);
			}else{
				if(Id=="temp_ip"+i){
					var fieldname=document.getElementById("ip_tempFNCopy").value;
					var fieldtype=document.getElementById("ip_tempFTCopy").value;
					var fieldlength=parseInt(document.getElementById("ip_tempFLCopy").value);
				}			
				var ip_row={};
				ip_row["fieldname"]=fieldname;
				ip_row["fieldtype"]=fieldtype;
				ip_row["fieldlength"]=fieldlength;
				inputArr.push(ip_row);
			}
			//inputArr.push(ip_row);
			document.getElementById("ipArr").value=JSON.stringify(inputArr);
			console.log(inputArr);
			console.log(document.getElementById("ipArr").value);
		}			
	}
	count=0;
	for(var i=0; i<tempop.length; i++){
		var Id=document.getElementsByClassName('tempopfld')[i].id;
		console.log(Id);		
		if(Id=="temp_op" || Id=="temp_op"+i){
			console.log(count);
			count++;
			console.log(count);
			if(count==1){
				if(Id=="temp_op"){
					var fieldname=document.getElementById("op_tempFN").value;
					var fieldtype=document.getElementById("op_tempFT").value;
					var fieldlength=parseInt(document.getElementById("op_tempFL").value);					
				}
				var op_row={};
				op_row["fieldname"]=fieldname;
				op_row["fieldtype"]=fieldtype;
				op_row["fieldlength"]=fieldlength;
				outputArr.push(op_row);
			}else{
				if(Id=="temp_op"+i){
					var fieldname=document.getElementById("op_tempFNCopy").value;
					var fieldtype=document.getElementById("op_tempFTCopy").value;
					var fieldlength=parseInt(document.getElementById("op_tempFLCopy").value);				
				}	
				var op_row={};
				op_row["fieldname"]=fieldname;
				op_row["fieldtype"]=fieldtype;
				op_row["fieldlength"]=fieldlength;
				outputArr.push(op_row);
			}
			document.getElementById("opArr").value=JSON.stringify(outputArr);
			console.log(outputArr);
			console.log(document.getElementById("opArr").value);
		}
	}
	var ip=JSON.parse(document.getElementById("ipArr").value);
	var op=JSON.parse(document.getElementById("opArr").value);
	var primerykey=document.getElementById("prikey").value;
	var webservice={};
	webservice["type"]="ws";
	webservice["primerykey"]=primerykey;
	webservice["input"]=ip;
	webservice["output"]=op;
	webservice["url"]=url;
	webservice["token"]=token;
	webservice["username"]=user;
	webservice["password"]=pass;
	
	return webservice;
}

function getExparamMailtempObj(url, token, user, pass){
	var mailtempip= document.getElementsByClassName("mailtempipfld");
	var mailtempop= document.getElementsByClassName("mailtempopfld");
	console.log(mailtempip.length);
	for(var i=0; i<mailtempip.length; i++){
		var Id=document.getElementsByClassName('mailtempipfld')[i].id;//0-prev val
		console.log(Id);
		if(Id=="mailtemp_ip" || Id=="mailtemp_ip"+i){
			console.log(count);
			count++;
			if(count==1){
				if(Id=="mailtemp_ip"){
					var fieldname=document.getElementById("ip_mailtempFN").value;
					var fieldtype=document.getElementById("ip_mailtempFT").value;
					var fieldlength=parseInt(document.getElementById("ip_mailtempFL").value);	
				}
				var ip_row={};
				ip_row["fieldname"]=fieldname;
				ip_row["fieldtype"]=fieldtype;
				ip_row["fieldlength"]=fieldlength;
				inputArr.push(ip_row);
			}else{
				if(Id=="mailtemp_ip"+i){
					var fieldname=document.getElementById("ip_mailtempFNCopy").value;
					var fieldtype=document.getElementById("ip_mailtempFTCopy").value;
					var fieldlength=parseInt(document.getElementById("ip_mailtempFLCopy").value);
				}			
				var ip_row={};
				ip_row["fieldname"]=fieldname;
				ip_row["fieldtype"]=fieldtype;
				ip_row["fieldlength"]=fieldlength;
				inputArr.push(ip_row);
			}
			//inputArr.push(ip_row);
			document.getElementById("ipArr").value=JSON.stringify(inputArr);
			console.log(inputArr);
			console.log(document.getElementById("ipArr").value);
		}			
	}
	count=0;
	for(var i=0; i<mailtempop.length; i++){
		var Id=document.getElementsByClassName('mailtempopfld')[i].id;
		console.log(Id);		
		if(Id=="mailtemp_op" || Id=="mailtemp_op"+i){
			console.log(count);
			count++;
			console.log(count);
			if(count==1){
				if(Id=="mailtemp_op"){
					var fieldname=document.getElementById("op_mailtempFN").value;
					var fieldtype=document.getElementById("op_mailtempFT").value;
					var fieldlength=parseInt(document.getElementById("op_mailtempFL").value);					
				}
				var op_row={};
				op_row["fieldname"]=fieldname;
				op_row["fieldtype"]=fieldtype;
				op_row["fieldlength"]=fieldlength;
				outputArr.push(op_row);
			}else{
				if(Id=="mailtemp_op"+i){
					var fieldname=document.getElementById("op_mailtempFNCopy").value;
					var fieldtype=document.getElementById("op_mailtempFTCopy").value;
					var fieldlength=parseInt(document.getElementById("op_mailtempFLCopy").value);				
				}	
				var op_row={};
				op_row["fieldname"]=fieldname;
				op_row["fieldtype"]=fieldtype;
				op_row["fieldlength"]=fieldlength;
				outputArr.push(op_row);
			}
			//outputArr.push(op_row);
			document.getElementById("opArr").value=JSON.stringify(outputArr);
			console.log(outputArr);
			console.log(document.getElementById("opArr").value);
		}
	}
	var ip=JSON.parse(document.getElementById("ipArr").value);
	var op=JSON.parse(document.getElementById("opArr").value);
	var primerykey=document.getElementById("prikey").value;
	/*{"Email":"doctiger@xyz.com","SFEmail":"user1@gmail.com","savetype":"new","ClauseId":"24","ClauseName":"C12","url":"http://json.parser.online.fr/",
	"externalparamobject":[{"type":"ws","primerykey":"Accno","input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"}],
	"output":[{"fieldname":"amount","fieldtype":"string","fieldlength":"255"}],"token":"yteywetryery","username":"u1","password":"pass"}]}*/
	var webservice={};
	webservice["type"]="ws";
	webservice["primerykey"]=primerykey;
	webservice["input"]=ip;
	webservice["output"]=op;
	webservice["url"]=url;
	webservice["token"]=token;
	webservice["username"]=user;
	webservice["password"]=pass;
	
	return webservice;
}
$('.web-services-add-temp-lib').click(function(){
	$('.web-services-temp-lib').toggleClass('display-block','display-none');
	var webUrl=document.getElementById("web-services-url-temp-lib").value;
	var uploadXls=document.getElementById("upload-xls-temp-lib").files.length;
	if(webUrl!=''){
		var a='';
		a=a+'<div class="row form-group">';
		a=a+'<div class="col-sm-3">';
		a=a+'<input type="text" class="form-control" value="'+webUrl+'"placeholder="URLName">';
		a=a+'</div>';
		a=a+'<div class="col-sm-3">';
		a=a+'<button class="btn btn-warning sm-btn-custom"><i class="fa fa-edit"></i></button>';
		a=a+'<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
		a=a+'</div>';
		a=a+'</div>';
		$('.add-url-name-main-temp-lib').append(a);
		$('.web-services-add-temp-lib input').val('');
		$('.web-services-add-temp-lib select').val('');
		
		var webservice= getExparamtempobj("web-services-url-temp-lib","temp_token","temp_user","temp_pass");
		exparamArr.push(webservice);
		console.log(exparamArr);
		document.getElementById("exparamArr").value=JSON.stringify(exparamArr);
		console.log(document.getElementById("exparamArr").value);
	}
	if(uploadXls!='0'){
		var a='';
		a=a+'<div class="row form-group">';
		a=a+'<div class="col-sm-3">';
		a=a+'<input type="text" class="form-control" value="URLName" placeholder="URLName">';
		a=a+'</div>';
		a=a+'<div class="col-sm-3">';
		a=a+'<button class="btn btn-warning sm-btn-custom"><i class="fa fa-edit"></i></button>';
		a=a+'<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
		a=a+'</div>';
		a=a+'</div>';
		$('.add-url-name-main-temp-lib').append(a);
		$('.web-services-add-temp-lib input').val('');
		$('.web-services-add-temp-lib select').val('');
		$('.web-services-add-temp-lib select').val('');
		document.getElementById("upload-xls-temp-lib").value="";
		$("#upload-xls-filename-temp-lib").text('FileName');
	}
});

var input3=document.getElementById('upload-xls-temp-lib');
var infoArea3=document.getElementById('upload-xls-filename-temp-lib');
//input3.addEventListener('change',showFileName3);

function showFileName3(event){
	var input3=event.srcElement;
	var fileName3=input3.files[0].name;
	infoArea3.textContent=fileName3;
}

$('.web-services-add-mail-temp').click(function(){
	$('.web-services-mail-temp').toggleClass('display-block','display-none');
	var webUrl=document.getElementById("web-services-url-mail-temp").value;
	var uploadXls=document.getElementById("upload-xls-mail-temp").files.length;
	if(webUrl!=''){
		var a='';
		a=a+'<div class="row form-group">';
		a=a+'<div class="col-sm-3">';
		a=a+'<input type="text" class="form-control" value="'+webUrl+'"placeholder="URLName">';
		a=a+'</div>';
		a=a+'<div class="col-sm-3">';
		a=a+'<button class="btn btn-warning sm-btn-custom"><i class="fa fa-edit"></i></button>';
		a=a+'<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
		a=a+'</div>';
		a=a+'</div>';
		$('.add-url-name-main-mail-temp').append(a);
		$('.web-services-add-mail-temp input').val('');
		$('.web-services-add-mail-temp select').val('');
		
		var webservice= getExparamMailtempobj("web-services-url-mail-temp","mailtemp_token","mailtemp_user","mailtemp_pass");
		exparamArr.push(webservice);
		console.log(exparamArr);
		document.getElementById("exparamArr").value=JSON.stringify(exparamArr);
		console.log(document.getElementById("exparamArr").value);
	}
	if(uploadXls!='0'){
		var a='';
		a=a+'<div class="row form-group">';
		a=a+'<div class="col-sm-3">';
		a=a+'<input type="text" class="form-control" value="URLName" placeholder="URLName">';
		a=a+'</div>';
		a=a+'<div class="col-sm-3">';
		a=a+'<button class="btn btn-warning sm-btn-custom"><i class="fa fa-edit"></i></button>';
		a=a+'<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
		a=a+'</div>';
		a=a+'</div>';
		$('.add-url-name-main-mail-temp').append(a);
		$('.web-services-add-mail-temp input').val('');
		$('.web-services-add-mail-temp select').val('');
		$('.web-services-add-mail-temp select').val('');
		document.getElementById("upload-xls-mail-temp").value="";
		$("#upload-xls-filename-mail-temp").text('FileName');
	}
});

var input4=document.getElementById('upload-xls-mail-temp');
var infoArea4=document.getElementById('upload-xls-filename-mail-temp');
//input4.addEventListener('change',showFileName4);

function showFileName4(event){
	var input4=event.srcElement;
	var fileName4=input4.files[0].name;
	infoArea4.textContent=fileName4;
}
$('#staticDynamicDoc').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		 //pallavi====
		  getTempforDoc();
		  //========
		$('.select-temp-dynamic-doc-main').css('display','block');
		$('.select-rule-dynamic-doc-main').css('display','none');
	}else{
		//pallavi====
		  getrulesfordoc("ruleforDynamic", "ruleforDoc");
		  //==========
		$('.select-temp-dynamic-doc-main').css('display','none');
		$('.select-rule-dynamic-doc-main').css('display','block');
	}
});
$('#dynamicDynamicDoc').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		//pallavi====
		  getrulesfordoc("ruleforDynamic", "ruleforDoc");
		  //==========
		$('.select-rule-dynamic-doc-main').css('display','block');
		$('.select-temp-dynamic-doc-main').css('display','none');
	}else{
		//pallavi==========
		  getTempforDoc();
		  //=============
		$('.select-rule-dynamic-doc-main').css('display','none');
		$('.select-temp-dynamic-doc-main').css('display','block');
	}
});
//ClauselibraryTab
$("body").on("click",".box-left.list-parth3",function(){
	$(this).parent('.list-part').find('ul').slideToggle();
});
$("body").on("click",".box-right.list-parth3",function(){
	$(this).parent('.list-part').find('ul').slideToggle();
});
$("body").on("click",".box-primary-key.list-parth3",function(){
	$(this).parent('.list-part').find('ul').slideToggle();
});
//TemplatelibraryTab
$("body").on("click",".box-left-temp-lib.list-parth3",function(){
	$(this).parent('.list-part').find('ul').slideToggle();
});
$("body").on("click",".box-right-temp-lib.list-parth3",function(){
	$(this).parent('.list-part').find('ul').slideToggle();
});
//MailTemplateTab
$("body").on("click",".box-left-mail-temp.list-parth3",function(){
	$(this).parent('.list-part').find('ul').slideToggle();
});
$("body").on("click",".box-right-mail-temp.list-parth3",function(){
	$(this).parent('.list-part').find('ul').slideToggle();
});
//ClauselibraryTab
$('.cls-lib-sfdc-previous-btn').click(function(){
	$('.select-sfdc-object-main').css('display','none');
	$('.createNewClauseMain').css('display','block');
});
$('.compose-clause-previous-btn').click(function(){
	if($('#external-parameter').is(':checked')){
		$('.compose-clause-main').css('display','none');
		$('.external-parameter-main').css('display','block');
	}else{
		$('.compose-clause-main').css('display','none');
		$('.select-sfdc-object-main').css('display','block');
	}
});
$('.cls-lib-external-para-previous-btn').click(function(){
	$('.external-parameter-main').css('display','none');
	$('.select-sfdc-object-main').css('display','block');
});
//TemplatelibraryTab
$('.mail-template-sfdc-previous-btn').click(function(){
	$('.select-sfdc-object-main-template').css('display','none');
	$('.createNewTempMain').css('display','block');
});
$('.compose-clause-temp-lib-pre-btn').click(function(){
	if($('#check-external-parameter-temp').is(':checked')){
		$('.compose-clause-main-template').css('display','none');
		$('.external-parameter-main-template').css('display','block');
	}else{
		$('.compose-clause-main-template').css('display','none');
		$('.select-sfdc-object-main-template').css('display','block');
	}
});

$('.temp-external-para-pre-btn').click(function(){
	$('.external-parameter-main-template').css('display','none');
	$('.select-sfdc-object-main-template').css('display','block');
});

//MailTemplateTab
$('.mail-temp-sfdc-pre-btn').click(function(){
	$('.select-sfdc-object-mail-temp-main').css('display','none');
	$('.createNewMailTempMain').css('display','block');
});

$('.mail-temp-compose-pre-btn').click(function(){
	if($('#external-parameter-mail-temp').is(':checked')){
		$('.compose-clause-mail-temp-main').css('display','none');
		$('.compose-clause-sms-temp-main').css('display','none');
		$('.external-parameter-mail-temp-main').css('display','block');
	}else{
		$('.compose-clause-mail-temp-main').css('display','none');
		$('.compose-clause-sms-temp-main').css('display','none');
		$('.select-sfdc-object-mail-temp-main').css('display','block');
	}
});
$('.mail-temp-external-pre-next').click(function(){
	$('.external-parameter-mail-temp-main').css('display','none');
	$('.select-sfdc-object-mail-temp-main').css('display','block');
});
$(document).ready(function(){
	
});
$(document).ready(function(){
	
});

//pallavi========================================

$("body").on("change", "#req-res-int-sub-event-id1", function () {
	//var checkR = $(this).val();
	
	//e.preventDefault();

//$("#req-res-int-sub-event-id1").change(function() {
    // Pure JS
    //var selectedVal = this.value;
    //var selectedText = this.options[this.selectedIndex].text;

    // jQuery
	//alert("in on select1");
    var eventname = $(this).val();  
    var eventid =$(this).find('option:selected').attr("eventid");
    console.log("eventname  "+eventname+"  eventid "+eventid);

    setprimaryOfEvent(eventname, eventid, "event-pk-id1", "event-pk-sfobj-id1" );  
    //alert("eventname  "+eventname+"  eventid "+eventid);

});

$("body").on("change", "#req-res-int-sub-event-id2", function () {
	//e.preventDefault();

//$("#req-res-int-sub-event-id2").change(function() {
    // Pure JS
    //var selectedVal = this.value;
    //var selectedText = this.options[this.selectedIndex].text;

    // jQuery
	//alert("in on select2");
    var eventname = $(this).val()
    var eventid =$(this).find('option:selected').attr("eventid");
    console.log("eventname  "+eventname+"  eventid "+eventid);
    setprimaryOfEvent(eventname, eventid, "event-pk-id2", "event-pk-sfobj-id2" );   
    //alert("eventname  "+eventname+"  eventid "+eventid);

});

//===============================================

$("body").on("click", ".create-new-document-btn-edit", function (e) {
	//pallavi===================
	//$('.create-new-document-btn-edit').click(function (e) {
		document.getElementById('DynamicTempType').value="edit";
	    e.preventDefault();
		  var $this = $(this);
		  //alert("in edit ");
			var copy = $(this).parents('.dymanicDocumentmain').find('.content-document-main');
			  //alert("copy "+copy);

		  var tempname = $this.attr("temp-name");
		  //alert(tempname);

		  editDynamicDoc(tempname, copy);
	    //===========================
	    $('.create-new-document-btn').css('display','none');
	    $('.content-document-main').css('display','block');
	    $('.content-btns-document-main').css('display','block');
	    $('.table-document-main').css('display','none');
	});
	//pallavi=========================
$("body").on("click", "#recIntegrationsubtab", function (e) {
	//alert("in rec-int sub");

	showeventlist("rec-int-sub-event", "rec-int-sub-event-id");
});
$("body").on("click", "#req-ressubtab", function (e) {
	//alert("in req-res sub");

	showeventlist("req-res-int-sub-event1", "req-res-int-sub-event-id1");

	showeventlist("req-res-int-sub-event2", "req-res-int-sub-event-id2");
});


//========================================


var ii=1;
$('body').on('click','.add-more-btn-workflow',function(){
	var copy=$(this).parents('.addMore').find('.addMore-copy').html();
	copy=copy.replace("**1**",ii);
	copy=copy.replace("**1**",ii);
	ii++;
	$(this).parents('.addMore').find('.addMore-main').append(copy);
	$('.selectpicker').selectpicker('refresh');
	$(this).parents('.addMore').find('.addMore-sub:last-child').find(".dropdown-toggle:first").css("display","none");
	$(this).parents('.addMore').find('.addMore-sub:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
});

var childJson={};
var childArr=[];
$('.add-more-btn-child-clause-name').click(function(){
/*{"ChildArray":[{"ClauseName":"GENa","DisplayName":"True","Metadata":"GENa","Description":"GENa","Multilingualdata":[{"language":"English","type":"online",
"para":["theUnitisnotowned,developedorsoldbytheLicensor."]},{"language":"Arabic","type":"online","para":["أنالوحدومملوكةومطورةومباعةمنقبلمانحالترخص"]}]}]}*/
	childJson= getChildJson();
	childArr.push(childJson);
	console.log(JSON.stringify(childArr));
	document.getElementById("childArr").value= JSON.stringify(childArr);
});

function getChildJson(){
	var ClauseName=document.getElementById("childClauseName").value;
	var DisplayName="True";
	var Metadata=document.getElementById("chldMD").value;
	var Description=document.getElementById("chldDes").value;
	
	if(ClauseName!=''){
		var a='';
		a='<a href="">'+ClauseName+'</a><br/>';
		$(this).parents('.addMore-main').find('.child-clause-name-list').append(a);
		$(this).parents('.addMore-main').find('.child-clause-name').val('');
		$(this).parents('.addMore-main').find('.meta-data-parent-cls-lib').val('');
		$(this).parents('.addMore-main').find('.description-parent-cls-lib').val('');
	}	
	childJson["ClauseName"]=ClauseName;
	childJson["DisplayName"]=DisplayName;
	childJson["Metadata"]=Metadata;
	childJson["Description"]=Description;
	var Multilingualdata= getMultilingualdata();
	/*console.log(mullagjson);
	childMulLangArr.push(mullagjson);
	console.log(childMulLangArr);
	document.getElementById("childMulLangArr").value= JSON.stringify(childMulLangArr);
	*/
	//childJson["Multilingualdata"]=JSON.parse(document.getElementById("childMulLangArr").value);
	childJson["Multilingualdata"]=Multilingualdata;
	console.log(JSON.stringify(childJson));
	return childJson;
} 

$("body").on("click",".add-more-btn-child-clause-name-sub",function(){
	var childCluaseName=$(this).parents('.addMore-main-in').find('.childClauseName').val();
	//console.log("1_1");
	if(childCluaseName!=''){
		var a='';
		a='<a href="">'+childCluaseName+'</a><br/>';
		$(this).parents('.addMore-main-in').find('.child-clause-name-list').append(a);
		$(this).parents('.addMore-main-in').find('.child-clause-name').val('');
		$(this).parents('.addMore-main-in').find('.meta-data-parent-cls-lib').val('');
		$(this).parents('.addMore-main-in').find('.description-parent-cls-lib').val('');
	}
});

$('#workflowCluseLib').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-clause-lib.select-workflow-parent-clause-lib').css('display','block');
	}else{
		$('.select-clause-lib.select-workflow-parent-clause-lib').css('display','none');
	}
});

$('#rulesBasedCluseLib').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-clause-lib .select-rule-parent-clause-lib').css('display','block');
	}else{
		$('.select-clause-lib .select-rule-parent-clause-lib').css('display','none');
	}
});

$('#controllerCluseLib').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.select-clause-lib .select-controller-parent-clause-lib').css('display','block');
	}else{
		$('.select-clause-lib .select-controller-parent-clause-lib').css('display','none');
	}
});

$('#workflowCluseLib1').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		getworkflow("Sel_workflow","mySelectWF");
		$('.above-advance-select .select-workflow-parent-clause-lib').css('display','block');
	}else{
		$('.above-advance-select .select-workflow-parent-clause-lib').css('display','none');
	}
});

$('#rulesBasedCluseLib1').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		//myFunction1();
		getrulesfordoc("Sel_Rule","mySelectRule");
		$('.above-advance-select .select-rule-parent-clause-lib').css('display','block');
	}else{
		$('.above-advance-select .select-rule-parent-clause-lib').css('display','none');
	}
});

$('#controllerCluseLib1').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		dropdown();
		/*http://35.221.183.246:8082/portal/servlet/service/GetController?Email=doctiger@xyz.com&SFEmail=doctiger8@gmail.com
		Method:-Get
		Output:{"status":"success","ControllerName":["Controller1","Controlle2","Controlle"]}
		*/
		var arr=[];
		$.ajax({
			type:'GET',
			url:'/portal/servlet/service/GetController?Email='+Email+'&SFEmail='+SFEmail+'&group='+group,
			async:true,
			success:function(dataa){
				//alert(dataa);
				var json=JSON.parse(dataa);
				var Controllers=json.ControllerName;
				arr=Controllers;
				console.log(arr);
				var myDiv=document.getElementById("Sel_Controller");
				console.log(arr);
				var selectList=document.createElement("select");
				selectList.id="mySelectCon";
				selectList.classList.add("form-control");
				selectList.classList.add("con-object");
				selectList.classList.add("bg-gray");
				selectList.classList.add("selectpicker");
				myDiv.appendChild(selectList);
				$("#mySelectCon").selectpicker();
				//selectList.setAttribute('class',"selectpicker");
				var opt=document.createElement("option");
				opt.text="SelectController";
				selectList.appendChild(opt);
				$("#mySelectCon").selectpicker("refresh");
				for(var i=0;i<arr.length;i++){
					var option=document.createElement("option");
					option.value=arr[i];
					option.text=arr[i];
					selectList.appendChild(option);
					$("#mySelectCon").selectpicker("refresh");
				}
			}
		});
		
		$('.above-advance-select .select-controller-parent-clause-lib').css('display','block');
	}else{
		$('.above-advance-select .select-controller-parent-clause-lib').css('display','none');
	}
	$("body").on("change","#mySelectCon",function(){
		console.log($(this).val());
		var heading=$(this).val();
		console.log(heading);
		document.getElementById("Con").value=heading;
		console.log(document.getElementById("Con").value);
	});
});

$("body").on("click",".parent-tr.cfa-open",function(){
	$(this).parents('tbody').find('.child-tr').slideToggle();
	$(this).parents('tbody').find('.sub-child-tr').slideUp();
	$(this).toggleClass('fa-minus');
});

$("body").on("click",".child-tr.cfa-open",function(){
	$(this).toggleClass('fa-minus');
	var a=$(this).parent('td').parent('.child-tr').attr('data-child');
	$(this).parents('tbody').find("[p-data-child='"+a+"']").slideToggle();
});

$('.cls-lib-new-cluse-previous-btn').click(function(){
	//getClauseTable();
	$('.createNewClauseMain').css('display','none');		
	$('.table-clause-library-main').show();
	$('.table-clause-library').show();
	$('.create-new-clause-btn').show();
});

$("body").on("click",".create-new-clause-btn-edit",function(){
	//alert("in click edit");
	document.getElementById("clsSaveType").value="edit";
	var $this = $(this);
	console.log($this);
	var clauseName = $this.attr("cls-name");
	document.getElementById("ParentClauseId").value= $this.attr("cls-id");
	//e.preventDefault();
	editClause(clauseName);	
});

function editClause(clauseName){
	console.log(clauseName);
	/*{"status":"success","Email":"doctiger_xyz.com","clauseId":"3","clauseName":"ClauseTest","Metadata":"اختبار جملة","Description":"description",
	 * "External Parameter":"false","Workflow":"WF1","RuleBased":"","Ruledata":"","Controller":"Controlle2","SFObject":{"DocTiger__Sales_Data__c":
	 * ["Id","OwnerId","IsDeleted"],"Primary Key":{"Object":"DocTiger__Sales_Data__c","key":"Id"}},"Multilingualdata":[{"language":"Arabic",
	 * "type":"online","para":["جملة باللغة العربية هذا هو وصف العبارة لاختبا"]}]}*/
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/EditParentClses?email='+Email+'&clauseName='+clauseName+'&group='+group,
		async: true,
		success:function(dataa){
			//alert(dataa);
			dataa = dataa.replace("External Parameter", "ExternalParam");
			var json=JSON.parse(dataa);
			console.log(json);
			console.log(json.clauseName);
			document.getElementById("cn").value=json.clauseName;
			document.getElementById("md").value=json.Metadata;
			document.getElementById("de").value=json.Description;
			
			if(json.ExternalParam=="true"){
				$('#external-parameter').prop("checked",true);
				document.getElementById("ws").style.display = "block";
				document.getElementById("web-services-url").value;
				document.getElementById("token").value;
				document.getElementById("user").value;
				document.getElementById("pass").value;
			}
			//document.getElementById("ExternalParam").value=json.ExternalParam;
			
			
			$('.create-new-clause-btn').css('display','none');
			$('.createNewClauseMain').css('display','block');
			$('.table-clause-library').css('display','none');
			$('.compose-clause-main').css('display','none');
			$('.select-sfdc-object-main').css('display','none');
			$('.external-parameter-main').css('display','none');
		}
	});
}

$("body").on("click",".create-new-ch-cls-btn-edit",function(){
	//alert("in click chedit");
	document.getElementById("chClsSaveType").value="edit";
	var $this = $(this);
	console.log($this);
	var clauseName = $this.attr("p-cls-name");
	document.getElementById("ParentClauseId").value= $this.attr("p-cls-id");
	var parentClsId= document.getElementById("ParentClauseId").value;
	var chClsName= $this.attr("cls-name");
	document.getElementById("chClauseId").value= $this.attr("cls-id");
	//document.getElementById("chClauseId").value= chClsName;
	var chClsId= document.getElementById("chClauseId").value;
	//e.preventDefault();
	editChClause(parentClsId, clauseName, chClsId, chClsName);	
});

function editChClause(ParentClauseId, clauseName, chClsId, chClsName){
	console.log(ParentClauseId+" "+clauseName+" "+chClsId+" "+chClsName);
/*{"status":"success","Email":"doctiger_xyz.com","ParentClauseId":"/content/user/doctiger_xyz.com/DocTigerAdvanced/Clauses/1/ChildClauses/0",
* "ParentClauseName":"child1","Metadata":"MD1","Description":"Des1","DisplayName":"","Workflow":"WF1","Multilingualdata":[{"language":"English",
* "type":"online","para":["The Licensor, owner or seller of the ."]},{"language":"Arabic","type":"online","para":["??????"]}]}*/
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/EditChildClses?email='+Email+'&ParentClauseId='+chClsId+'&group'+group,
		async: true,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			document.getElementById("parentCls").value= clauseName;
			document.getElementById("childClauseName").value= json.ParentClauseName;
			document.getElementById("chldMD").value=json.Metadata;
			document.getElementById("chldDes").value=json.Description;
			var Multilingualdata=[];
			Multilingualdata= json.Multilingualdata;
			
			$('.create-new-clause-btn').css('display','none');
			$('.createNewClauseMain').css('display','none');
			$('.parent-clause-main').css('display','block');
			$('.table-clause-library').css('display','none');
			$('.compose-clause-main').css('display','none');
			$('.select-sfdc-object-main').css('display','none');
			$('.external-parameter-main').css('display','none');
		}
	});
}

$("body").on("click",".create-new-template-btn-edit",function(){
	//alert("in temp edit");
	document.getElementById("tempSaveType").value="edit";
	var $this = $(this);
	console.log($this);
	var tempName = $this.attr("temp-name");
	//document.getElementById("ParentClauseId").value= $this.attr("cls-id");
	//e.preventDefault();
	editTemplate(tempName);	
});

function editTemplate(tempName){
	console.log(tempName);
	/*http://35.221.183.246:8082/portal/servlet/service/EditTemplate?email=doctiger@xyz.com&template=temp1*/
	/*{"status":"success","email":"doctiger@xyz.com","template":"temp1","saveType":"edit","created_Date":"Tue Nov 13 08:50:48 UTC 2018",
	 * "flag":1,"version":"0.1","metadata":"m2","description":"descr","SFobject":{"Account":["accname","Accno"],
	 * "contact":["contactname","contactno"],"Primary Key":{"Object":"Account","key":"Accno"}},"externalparamobject":[{"type":"ws",
	 * "primerykey":"Accno","url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1","password":"pass",
	 * "input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string","fieldlength":"255"},
	 * {"fieldname":"contactno","fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":"amount","fieldtype":"string",
	 * "fieldlength":"255"},{"fieldname":"invoiceno","fieldtype":"string","fieldlength":"255"}]}],"selectedclause":["C1","C2"],
	 * "uploadtemplate":{"filename":"abc.docx"},"advancedobject":{"selectedRule":"rule1","selectedWorkflow":"workflow1",
	 * "selectedWorkflowForDoc":"workflow1"}}*/
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/EditTemplate?email='+Email+'&template='+tempName+'&group='+group,
		async: true,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			console.log(json.template);
			var tempName= json.template;
			var tempMD= json.metadata;
			var tempDes= json.description;
			document.getElementById("tempName").value=tempName;
			document.getElementById("tempMD").value=tempMD;
			document.getElementById("tempDes").value=tempDes;
			
			console.log(tempName+" "+tempMD+" "+tempDes);						
			$('.create-new-template-btn').css('display','none');
			$('.table-clause-template').css('display','none');
			$('.table-clause-template-main').css('display','none');
			$('.compose-clause-main-template').css('display','none');
			$('.select-sfdc-object-main-template').css('display','none');
			$('.external-parameter-main-template').css('display','none');
			$('.createNewTempMain').css('display','block');
			console.log("data");
		}	
	});
}

$("body").on("click",".create-new-mail-template-btn-edit",function(){
	//alert("in mailtemp edit");
	document.getElementById("mailtempSaveType").value="edit";
	var $this = $(this);
	console.log($this);
	var tempName = $this.attr("mail-temp-name");
	var tempType = $this.attr("temp-type");
	if(tempType== "mail"){
		editMailTemp(tempName);
	}else if(tempType== "sms"){
		 $("#sms-temp").prop("checked", true);
		 $('.temp-name').attr('id','smsName');
		 editSmsTemp(tempName);
	}		
});

function editMailTemp(tempName){
	console.log(tempName);
	/*http://35.221.183.246:8082/portal/servlet/service/EditMailTemplate?email=doctiger@xyz.com&mailtemplate=Mail temp*/
	/*{"status":"success","email":"doctiger@xyz.com","template":"Mail temp","Creation Date":"Tue Nov 13 10:44:13 UTC 2018",
	 * "flag":1,"version":"1.0","metadata":"m2","description":"Des1","Body":"body skjfhhgf","From":"fromid","Subject":"subjet string",
	 * "To":"toid","SFobject":{"Add":["accname","Accno"],"cdd":["contactname","contactno"],"Primary Key":{"Object":"Account","key":"Accno"}},
	 * "externalparamobject":[{"type":"ws","primerykey":"Accno","url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1",
	 * "password":"pass","input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string",
	 * "fieldlength":"255"},{"fieldname":"contactno","fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":"amount",
	 * "fieldtype":"string","fieldlength":"255"},{"fieldname":"invoiceno","fieldtype":"string","fieldlength":"255"}]},{"type":"ws",
	 * "primerykey":"Accno","url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1","password":"pass",
	 * "input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string","fieldlength":"255"},
	 * {"fieldname":"contactno","fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":"amount","fieldtype":"string",
	 * "fieldlength":"255"},{"fieldname":"invoiceno","fieldtype":"string","fieldlength":"255"}]},{"type":"ws","primerykey":"Accno",
	 * "url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1","password":"pass","input":[{"fieldname":"name",
	 * "fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string","fieldlength":"255"},{"fieldname":"contactno",
	 * "fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":"amount","fieldtype":"string","fieldlength":"255"},
	 * {"fieldname":"invoiceno","fieldtype":"string","fieldlength":"255"}]}],"filename":"abc.docx","advancedobject":{"selected Rule":"rule1",
	 * "Ruledata":"{\"URL\":\"http://35.236.213.87:8080/drools/callrules/doctiger@xyz.com_doctiger_project_rulesdoctiger/fire\",\"Input\":[],\"Output\":[]}"}}*/
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/EditMailTemplate?email='+Email+'&mailtemplate='+tempName+'&group='+group,
		async: true,
		success:function(dataa){
			//alert(dataa);
			var tempData= dataa.replace("Creation Date", "Creation_Date");
			tempData= tempData.replace("Created_Date", "Creation_Date");
			var json=JSON.parse(tempData);
			console.log(json);
			console.log(json.template);
			
			document.getElementById("mailName").value= json.template;
			document.getElementById("mailTempMD").value= json.metadata;
			document.getElementById("mailTempDes").value= json.description;
			
			$('.create-new-mail-template-btn').css('display','none');
			$('.createNewMailTempMain').css('display','block');
			$('.table-mail-template').css('display','none');
			$('.compose-clause-mail-temp-main').css('display','none');
			$('.select-sfdc-object-mail-temp-main').css('display','none');
			$('.external-parameter-mail-temp-main').css('display','none');
			console.log("data");
		}	
	});
}

function editSmsTemp(tempName){
	console.log(tempName);
	/*http://35.221.183.246:8082/portal/servlet/service/EditSMSTemplate?email=doctiger@xyz.com&smstemplate=SMSTemp1*/
	/*{"status":"success","email":"doctiger@xyz.com", "SFEmail":"doctiger@gmail.com" ,"template":"SMS Temp1","saveType":"edit",
	 * "created_Date":"Tue Sep 11 12:03:57 UTC 2018","flag":1,"version":"0.1","metadata":"mm11","description":"gg",
	 * "SFobject":{"Account":["accnameww","Accnoww"],"contact":["contactname","contactno"],"Primary Key":{"Object":"Account","key":"Accno"}},
	 * "externalparamobject":[{"type":"ws","primerykey":"Accno","url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1",
	 * "password":"pass","input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string",
	 * "fieldlength":"255"},{"fieldname":"contactno","fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":"amount",
	 * "fieldtype":"string","fieldlength":"255"},{"fieldname":"invoiceno","fieldtype":"string","fieldlength":"255"}]},{"type":"ws",
	 * "primerykey":"Accno222","url":"http://json.parser.online.fr/","token":"yteywetryery","username":"u1","password":"pass",
	 * "input":[{"fieldname":"name","fieldtype":"string","fieldlength":"255"},{"fieldname":"email","fieldtype":"string","fieldlength":"255"},
	 * {"fieldname":"contactno","fieldtype":"string","fieldlength":"255"}],"output":[{"fieldname":"amount","fieldtype":"string",
	 * "fieldlength":"255"},{"fieldname":"invoiceno","fieldtype":"string","fieldlength":"255"}]}],"Body":"ss","From":"abc@gmail”,"To":"dd"}*/
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/EditSMSTemplate?email='+Email+'&smstemplate='+tempName+'&group='+group,
		async: true,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			console.log(json.template);
			
			document.getElementById("smsName").value= json.template;
			document.getElementById("mailTempMD").value= json.metadata;
			document.getElementById("mailTempDes").value= json.description;
			
			$('.create-new-mail-template-btn').css('display','none');
			$('.createNewMailTempMain').css('display','block');
			$('.table-mail-template').css('display','none');
			$('.compose-clause-mail-temp-main').css('display','none');
			$('.select-sfdc-object-mail-temp-main').css('display','none');
			$('.external-parameter-mail-temp-main').css('display','none');
			console.log("data");
		}	
	});
}

$("body").on("click",".delete-parent-cls",function(){
	/*//alert("in click edit");
	document.getElementById("clsSaveType").value="edit";
	var $this = $(this);
	console.log($this);
	var clauseName = $this.attr("cls-name");
	document.getElementById("ParentClauseId").value= $this.attr("cls-id");
	//e.preventDefault();
	editClause(clauseName);	*/
	/*var clickedrow=$(this).closest('tr');
	console.log(clickedrow);
	var rowId=clickedrow.attr("id");
	console.log(rowId);
	var cellVal=document.getElementById(rowId).cells[0].innerHTML;
	console.log(cellVal);
	var clauseName=cellVal.toString().substring(0,cellVal.toString().indexOf("<"));
	*/
	var $this = $(this);
	console.log($this);
	var clauseName = $this.attr("cls-name");
	console.log(clauseName);
	var clauseId= $this.attr("cls-id");
	//$.ajax({
		/*type:'GET',
		url:'/portal/servlet/service/GetClsByName?email='+Email+'&clauseName='+clauseName,
		async: true,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			console.log(json.clauseId);
			clauseId=json.clauseId;
		},
		complete:function(){*/
			$.ajax({
				type:'GET',
				url:'/portal/servlet/service/DelelteClause?email='+Email+'&parentclauseid='+clauseId+'&group='+group,
				async: true,
				success:function(dataa){
					//alert(dataa);
					var json=JSON.parse(dataa);
					console.log(json);	
					getClauseTable();
					location.reload(true);
					/*$('.createNewClauseMain').css('display','none');
					$('.select-sfdc-object-main').css('display','none');
					$('.external-parameter-main').css('display','none');
					$('.compose-clause-main').css('display','none');
					$('.table-clause-library').show();
					$('.table-clause-library-main').css('display','block');
					$('.create-new-clause-btn').css('display','block');*/
				}
			});			
	});

$("body").on("click",".delete-temp",function(){
	//alert("onCLick Delete");
	var $this = $(this);
	console.log($this);
	var tempName = $this.attr("temp-name");
	console.log(tempName);
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/DeleteTemplate?email='+Email+'&template='+tempName+'&group='+group,
		async: true,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			console.log(json);
			getTemplateTable();
		}
	});
});

$("body").on("click",".delete-mailTemp",function(){
	//alert("onCLick Delete");
	var $this = $(this);
	console.log($this);
	var tempName = $this.attr("mail-temp-name");
	var tempType = $this.attr("temp-type");
	console.log(tempName);
	console.log(tempType);
	if(tempType=="mail"){
		$.ajax({
			type:'GET',
			url:'/portal/servlet/service/DeleteMailTemplate?email='+Email+'&mailtemplate='+tempName+'&group='+group,		
			async: true,
			success:function(dataa){
				//alert(dataa);
				var json=JSON.parse(dataa);
				console.log(json);
				getMailTempTable();
			}
		});
	}else if(tempType=="sms"){
		$.ajax({
			type:'GET',
			url:'/portal/servlet/service/DeleteSMSTemplate?email='+Email+'&smstemplate='+tempName+'&group='+group,
			async: true,
			success:function(dataa){
				//alert(dataa);
				var json=JSON.parse(dataa);
				console.log(json);
				getMailTempTable();
			}
		});
	}
	
});
$('.parent-clause-previous-btn').click(function(){
	$('.parent-clause-main').css('display','none');
	$('.compose-clause-main').css('display','block');
});
$('.temp-lib-create-new-previous-btn').click(function(){
	$('.createNewTempMain').css('display','none');
	$('.table-clause-template-main').show();
	$('.table-clause-template').show();
	$('.create-new-template-btn').show();
});
$('#checkAdvancedComposeTempLib').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		//alert("test");
		//myFunction1();
		getrulesfordoc("Sel_RuleTemp","temp_selRule");
		getworkflow("Sel_WFTemp","temp_selWF");
		getworkflowfordoc("Sel_WFDocTemp","temp_selWFDoc");
		$('.select-rule-compose-main-temp-lib').css('display','block');
		$('.select-workflow-compose-main-temp').css('display','block');
		$('.select-workflow-compose-main-temp').css('display','block');
	}else{
		$('.select-rule-compose-main-temp-lib').css('display','none');
		$('.select-workflow-compose-main-temp').css('display','none');
		$('.select-workflow-compose-main-temp').css('display','none');
	}
});
$('.mail-temp-new-pre-btn').click(function(){
	$('.createNewMailTempMain').css('display','none');
	$('.table-mail-template-main').show();
	$('.table-mail-template').show();
	$('.create-new-mail-template-btn').show();
});
$('#checkAdvancedComposeMailTemp').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		getrulesfordoc("sel_RuleMailTemp","Mailtemp_selRule");
		getworkflow("Sel_WFMailTemp","Mailtemp_selWF");
		
		$('.select-rule-compose-mail-temp').css('display','block');
		$('.select-workflow-compose-mail-temp').css('display','block');
		$('.select-workflow-compose-mail-temp').css('display','block');
	}else{
		$('.select-rule-compose-mail-temp').css('display','none');
		$('.select-workflow-compose-mail-temp').css('display','none');
		$('.select-workflow-compose-mail-temp').css('display','none');
	}
});

document.getElementById('upload-attachment').addEventListener('change', getBase64, false);

function getBase64(e) {
var file  = e.target.files[0];
console.log(file);
var infoArea5=document.getElementById('upload-attachment-filename');
var fileName5=file.name;
infoArea5.textContent=fileName5;
document.getElementById("upfiledoc").value= fileName5;
console.log(fileName5);

var reader = new FileReader();
reader.readAsDataURL(file);
reader.onloadend  = function (e) {
console.log(reader.result);
document.getElementById("base64mailtemp").value=reader.result;
};

}

var input6=document.getElementById('upload-excel');
var infoArea6=document.getElementById('upload-excel-filename');
//input6.addEventListener('change',showFileName6);

function showFileName6(event){
	var input6=event.srcElement;
	var fileName6=input6.files[0].name;
	infoArea6.textContent=fileName6;
}
var input7=document.getElementById('upload-excel-parent-cls-lib');
var infoArea7=document.getElementById('upload-excel-parent-cls-lib-filename');
//input7.addEventListener('change',showFileName7);
function showFileName7(event){
	var input7=event.srcElement;
	var fileName7=input7.files[0].name;
	infoArea7.textContent=fileName7;
}



//varinput8=document.getElementById('upload-excel-ws-integration');
//varinfoArea8=document.getElementById('upload-excel-ws-integration-filename');
//input8.addEventListener('change',showFileName8);
function showFileName8(event){
	var input8=event.srcElement;
	var fileName8=input8.files[0].name;
	infoArea8.textContent=fileName8;
}
$('.upload_file_parent_cls_lib').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.upload-file-main-parnt-cls-lib').css('display','block');
		$('.compose-online-main-parnt-cls-lib').css('display','none');
	}
});
$('.compose_online_parent-cls-lib').click(function(){
	var $this=$(this);
	if($this.is(':checked')){
		$('.compose-online-main-parnt-cls-lib').css('display','block');
		$('.upload-file-main-parnt-cls-lib').css('display','none');
	}
});
$('.create-new-document-btn').click(function(e){
	//pallavi==========
	document.getElementById('DynamicTempType').value="new";
	//=================================
	e.preventDefault();
	$('.create-new-document-btn').css('display','none');
	$('.content-document-main').css('display','block');
	$('.content-btns-document-main').css('display','block');
	$('.table-document-main').css('display','none');
});
$('.document-previous-btn').click(function(){
	//pallavi=================
	dislayDocTable();
	//========================
	$('.content-document-main').css('display','none');
	$('.content-btns-document-main').css('display','none');
	$('.table-document-main').show();
	$('.create-new-document-btn').show();
});
var dropdown=function getDropdown(){
	console.log("Dropdownhere");
}

$("body").on("click", ".deleteDynamicDoc", function (e) {
	e.preventDefault();

	  var $this = $(this);
  var tempname = $this.attr("temp-name");
  //alert(tempname);

 $.ajax({
	   type: 'GET',
	   url: '/portal/servlet/service/DeleteAdvancedTemplate?email='+Email+'&template='+tempname+'&group='+group,
	   success: function (dataa) {
	    //alert(dataa);
	   }
  });
 dislayDocTable();
 $('.create-new-document-btn').css('display','block');
 $('.content-document-main').css('display','none');
 $('.content-btns-document-main').css('display','none');
 $('.table-document-main').css('display','block');
 //alert("end of delete click");
  
});
//========================

//pallavi==========================
function getTempforDoc(){
	//http://35.201.178.201:8082/portal/servlet/service/GetTemplates?email=user@gmail.com
	//Output: {"status":"success","Templates":["temp1","temp2","temp3"]} 
	document.getElementById("tempforDynamic").innerHTML = "";
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetTemplates?email=' + Email + '&SFEmail=' + SFEmail+'&group='+group,
		success: function (dataa) {
			try{
				var json = JSON.parse(dataa);
				var WorkFlows = json.Templates;
				console.log(WorkFlows);
				var myDiv = document.getElementById("tempforDynamic");
				var selectList = document.createElement("select");
				selectList.id = "mySelecttemp";
				
				selectList.classList.add("form-control");
				selectList.classList.add("sf-object");
				selectList.classList.add("bg-gray");
				selectList.classList.add("selectpicker");
	  	
				myDiv.appendChild(selectList);
				$("#mySelecttemp").selectpicker();
				var opt = document.createElement("option");
				opt.text = "Select Template";
				selectList.appendChild(opt);
				$("#mySelecttemp").selectpicker("refresh");
				for (var i = 0; i < WorkFlows.length; i++) {
					var option = document.createElement("option");
					option.value = WorkFlows[i];
					option.text = WorkFlows[i];
					selectList.appendChild(option);
					$("#mySelecttemp").selectpicker("refresh");
				}
			}catch(err){
				console.log(err);
			}			
		}
	});
}

function getrulesfordoc(divid,idforselec){
/*http://35.221.183.246:8082/portal/servlet/service/GetRulenWorkflow.rules?Email=doctiger@xyz.com&SFEmail=doctiger8@gmail.com
*{"status":"success","Rules":[{"Rule_Engine":"Rule1","Rule_data":"{\"URL\":\"http://35.236.213.87:8080/drools/callrules/carrotrule_xyz.com_Project1_Rule1
*fire\",\"input\":[{\"field\":\"employeeID\",\"type\":\"Integer\"},{\"field\":\"saleVal\",\"type\":\"Integer\"},{\"field\":\"salesPerson\",\"type\":\"String\"},
*{\"field\":\"pricevalue\",\"type\":\"String\"}],\"output\":[{\"field\":\"OutputF1\"},{\"field\":\"OutputF2\"},{\"field\":\"OutputF3\"}]}"}]}*/
	document.getElementById(divid).innerHTML="";
	console.log(idforselec);
	var Rules;
	try{
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/GetRulenWorkflow.rules?Email='+Email+'&SFEmail='+SFEmail+'&group='+group,
		success:function(dataa){
			console.log(dataa);
			var json=JSON.parse(dataa);
			Rules=json.Rules;
			console.log(Rules);
			var myDiv=document.getElementById(divid);
			console.log(myDiv);
			var selectList=document.createElement("select");
			selectList.id=idforselec;
			selectList.classList.add("form-control");
			selectList.classList.add("rule-object");
			selectList.classList.add("bg-gray");
			selectList.classList.add("selectpicker");
			myDiv.appendChild(selectList);
			$("#"+idforselec).selectpicker();
			var opt=document.createElement("option");
			opt.text="SelectRule";
			selectList.appendChild(opt);
			$("#"+idforselec).selectpicker("refresh");
			for(var i=0;i<Rules.length;i++){
				var option=document.createElement("option");
				option.value=Rules[i].Rule_Engine;
				option.text=Rules[i].Rule_Engine;
				option.setAttribute("data-ruledata",Rules[i].Rule_data);
				selectList.appendChild(option);
				$("#"+idforselec).selectpicker("refresh");
			}
		}
	});}
	catch(e){
		console.log("Error:"+e.description);																				
	}
	console.log(idforselec);
	$("body").on("change","#"+idforselec,function(){
		console.log($(this).val());
		var heading=$(this).val();
		console.log(heading);
		document.getElementById("RuleEngine").value=heading;
		console.log(document.getElementById("RuleEngine").value);
		var chk=heading;
		for(var i=0;i<Rules.length;i++){
			if(Rules[i].Rule_Engine==chk){
				var rd=Rules[i].Rule_data;
				console.log(rd);
				document.getElementById("RuleData").value=rd;
				console.log(document.getElementById("RuleData").value);
			}else{
				
			}
		}
	});
}

function getworkflow(divid,idforselec){
	document.getElementById(divid).innerHTML="";
	console.log(divid);
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/GetRulenWorkflow.workflows?Email='+Email+'&SFEmail='+SFEmail+'&group='+group,
		success:function(dataa){
			var json=JSON.parse(dataa);
			var WorkFlows=json.WorkFlows;
			console.log(WorkFlows);
			var myDiv=document.getElementById(divid);
			var selectList=document.createElement("select");
			selectList.id=idforselec;
			selectList.classList.add("form-control");
			selectList.classList.add("wf-object");
			selectList.classList.add("bg-gray");
			selectList.classList.add("selectpicker");
			myDiv.appendChild(selectList);
			console.log(selectList);
			$("#"+idforselec).selectpicker();
			var opt=document.createElement("option");
			opt.text="SelectWorkflow";
			selectList.appendChild(opt);
			$("#"+idforselec).selectpicker("refresh");
			for(var i=0;i<WorkFlows.length;i++){
				var option=document.createElement("option");
				option.value=WorkFlows[i];
				option.text=WorkFlows[i];
				selectList.appendChild(option);
				$("#"+idforselec).selectpicker("refresh");
			}
		}
	});
	
	$("body").on("change","#"+idforselec,function(){
		console.log($(this).val());
		var heading=$(this).val();
		console.log(heading);
		document.getElementById("wf_sel").value=heading;
		console.log(document.getElementById("wf_sel").value);
	});
}

function getworkflowfordoc(divid,idforselec){
	document.getElementById(divid).innerHTML="";
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/GetRulenWorkflow.documentworkflow?Email='+Email+'&SFEmail='+SFEmail+'&group='+group,
		success:function(dataa){
			//alert(dataa);
			var json=JSON.parse(dataa);
			var WorkFlows=json.DocumnetWorkFlows;
			console.log(WorkFlows);
			var myDiv=document.getElementById(divid);
			var selectList=document.createElement("select");
			selectList.id=idforselec;
			selectList.classList.add("form-control");
			selectList.classList.add("wfdoc-object");
			selectList.classList.add("bg-gray");
			selectList.classList.add("selectpicker");
			myDiv.appendChild(selectList);
			$("#"+idforselec).selectpicker();
			var opt=document.createElement("option");
			opt.text="SelectWorkflow";
			selectList.appendChild(opt);
			$("#"+idforselec).selectpicker("refresh");
			for(var i=0;i<WorkFlows.length;i++){
				var option=document.createElement("option");
				option.value=WorkFlows[i];
				option.text=WorkFlows[i];
				selectList.appendChild(option);
				$("#"+idforselec).selectpicker("refresh");
			}
		}
	});
	
	$("body").on("change","#"+idforselec,function(){
		console.log($(this).val());
		var heading=$(this).val();
		console.log(heading);
		document.getElementById("wfdoc_sel").value=heading;
		console.log(document.getElementById("wfdoc_sel").value);
	});
}				

function getcontrollerfordoc( divid,  idforselec){
/* http://35.221.183.246:8082/portal/servlet/service/GetController?Email=doctiger@xyz.com&SFEmail=doctiger8@gmail.com
Output: {"status":"success","Controller Name":["Controller1","Controlle2","Controlle"]}	*/	
	document.getElementById(divid).innerHTML = "";
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetController?Email=' + Email + '&SFEmail=' + SFEmail+'&group='+group,
		success: function (dataa) {
			dataa = dataa.replace("Controller Name", "Controller_Name");
			console.log("data after replace "+dataa);
			var json = JSON.parse(dataa);
			var Controller_Name = json.Controller_Name;
			console.log(Controller_Name);
			var myDiv = document.getElementById(divid);
			var selectList = document.createElement("select");
			selectList.id = idforselec;
			selectList.classList.add("form-control");
			selectList.classList.add("sf-object");
			selectList.classList.add("bg-gray");
			selectList.classList.add("selectpicker");
	
			myDiv.appendChild(selectList);
			$("#"+idforselec).selectpicker();
			var opt = document.createElement("option");
			opt.text = "Select Controller";
			selectList.appendChild(opt);
			$("#"+idforselec).selectpicker("refresh");
			for (var i = 0; i < ControllerName.length; i++) {
				var option = document.createElement("option");
				option.value = ControllerName[i];
				option.text = ControllerName[i];
				selectList.appendChild(option);
				$("#"+idforselec).selectpicker("refresh");
			}
		}
	});
}

$('#DynamicDocsave').click(function () {
//	API: - http://35.201.178.201:8082/portal/servlet/service/SaveAdvancedTemplate
//		Input: {"email":"user@gmail.com","username":"user@gmail.com","dynamicTemplatename":"templated","outputFormat":"pdf","static":"true/false","dynamic":"true/false","selectedTemplate":"template1","selectedRule":"m1","advanced":"true/false","selectedController":"contr","selectedWorkflow":"workflow1","type":"new"}
	var $this = $(this);  
	var copy = $(this).parents('.dymanicDocumentmain').find('.content-document-main');
		
	var jsonData = {};
		 
	jsonData["email"]= Email;
	jsonData["group"]= group;
	jsonData["username"]= Email;
	var type= copy.find('#DynamicTempType').val();
	jsonData["type"]= copy.find('#DynamicTempType').val();

	var dynamicTemplatename = copy.find('#document_template_id').val();
	console.log("dynamicTemplatename "+dynamicTemplatename);
	jsonData["dynamicTemplatename"] = dynamicTemplatename;
	var outputFormat = copy.find('#document_outputFormat').val();
	console.log("outputFormat "+outputFormat);
	jsonData["outputFormat"] = outputFormat;
	
	if ($('.static-dynamic-doc').is(':checked')) {
		var doc_static=copy.find('#staticDynamicDoc').val();
		console.log("doc_static  "+doc_static);
		var selectedTemplate=copy.find('#mySelecttemp').val();
		console.log("selectedTemplate "+selectedTemplate);
		jsonData["static"]="true";
		jsonData["selectedTemplate"]=selectedTemplate;
	}else{
		jsonData["static"]="false";
	}

	if ($('.dynamic-dynamic-doc').is(':checked')) {
		var doc_dynamic=copy.find('#staticDynamicDoc').val();
		console.log("doc_dynamic "+doc_dynamic);
		var selectedRule=copy.find('#ruleforDoc').val();
		console.log("selectedRule "+selectedRule);
		var Ruledata = $("#ruleforDoc option:selected").attr("data-ruledata");
		console.log("Ruledata1  "+Ruledata);
		
		jsonData["Ruledata"]=Ruledata;
		jsonData["dynamic"]="true";
		jsonData["selectedRule"]=selectedRule;
	}else{
		jsonData["dynamic"]="false";
	}

	if ($('.advanced-dynamic-doc').is(':checked')) {
		var advanced=copy.find('#advanced-dynamic-doc-id').val();
		console.log("advanced "+advanced);
		var selectedController=copy.find('#controllerforDoc').val();
		console.log("selectedController "+selectedController);
		var selectedWorkflow=copy.find('#workflowforDoc').val();
		console.log("selectedWorkflow "+selectedWorkflow);
		
		jsonData["advanced"]="true";
		jsonData["selectedController"]=selectedController;
		jsonData["selectedWorkflow"]=selectedWorkflow;
	}else{
		jsonData["advanced"]="false";
	}
	
	console.log("jsonData "+JSON.stringify(jsonData));
	$.ajax({
		type: 'POST',
		url: '/portal/servlet/service/SaveAdvancedTemplate',
		async:true,
		data:JSON.stringify(jsonData),
		dataType: 'json',
		contentType: 'application/json',
		success: function (dataa) {
			var status=dataa.status;
			var message=dataa.message;
			if(status=="success"){
				document.getElementById('DynamicTempType').value="new";
				document.getElementById('Dynamicerrormessage').innerHTML ="";
				dislayDocTable();
				$('.create-new-document-btn').css('display','block');
				$('.content-document-main').css('display','none');
		        $('.content-btns-document-main').css('display','none');
		        $('.table-document-main').css('display','block');
		    }else if(status="error"){
		    	document.getElementById('Dynamicerrormessage').innerHTML =message;
		    }
	   }
   });
});

function dislayDocTable(){
	console.log("in display doctable");
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetAdvancedTemplates?email='+Email+'&group='+group,
		success: function (dataa) {
			try{
				var tempData= dataa.replace("Created_Date", "Creation_Date");
				var json = JSON.parse(tempData);
			    console.log(json);
			    var  AdvancedTemplates = json.AdvancedTemplates;
			    console.log(AdvancedTemplates);
			    var tab =document.getElementById("DynamicDocTable");
			    var oTBody = tab.getElementsByTagName("tbody")[0];
			    oTBody.innerHTML = "";  
			   
			    var table = document.getElementById("DynamicDocTable");
			    var tablebody = table.getElementsByTagName("tbody")[0];
			    for (var i = 0; i < AdvancedTemplates.length; i++) {
			    	var count =i-1;
			    	var DTName = AdvancedTemplates[i].Template;
			    	var version = AdvancedTemplates[i].Version;
			    	var creDate = AdvancedTemplates[i].Created_Date;
			    	var createdby=AdvancedTemplates[i].Created_By;
			    	var action ='<td class="action-btn"><button class="btn btn-bg-brown btn-sm create-new-document-btn-edit" temp-name="'+DTName+'"><i class="fa fa-pencil"></i></button><button  class="btn btn-bg-light-danger btn-sm deleteDynamicDoc" temp-name="'+DTName+'"><i class="fa fa-trash"></i></button><a href="" class="btn btn-bg-light-blue btn-sm"><i class="fa fa-eye"></i></a></td>';
			    	console.log(DTName);
			    	var row = tablebody.insertRow(count + 1);
			    	var cell1 = row.insertCell(0);
			    	var cell2 = row.insertCell(1);
			    	var cell3 = row.insertCell(2);
			    	var cell4 = row.insertCell(3);
			    	var cell5 = row.insertCell(4);
			    	var cell6 = row.insertCell(5);
			    	var cell7 = row.insertCell(6);
			    	
			    	cell1.innerHTML = DTName ;
			    	cell2.innerHTML = "PDF";
			    	cell3.innerHTML = createdby;
			    	cell4.innerHTML = "";
			    	cell5.innerHTML = creDate;
			    	cell6.innerHTML = version;
			    	cell7.innerHTML = action;
			    }
			}catch(err){
				console.log(err);
			}
					
//{"status":"success","AdvancedTemplates":[{"Template":"templated","Created_Date":"Sat Jul 21 11:33:00 IST 2018","Created_By":"user@gmail.com",
//"Approved_By":"admin","selectedWorkflow":"workflow1","selectedRule":"m1"},{"Template":"templated1","Created_Date":"Sat Jul 21 11:33:08 IST 2018",
//"Created_By":"user@gmail.com","Approved_By":"admin","selectedWorkflow":"workflow1","selectedRule":"m1"}]} 
		    
		}
	});
}

function editDynamicDoc(tempname, copy){
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/EditAdvancedTemplate?email='+Email+'&template='+tempname+'&group='+group,
		success: function (dataa) {
			var json = JSON.parse(dataa);
			console.log( "json.hasOwnProperty('status')  "+json.hasOwnProperty('status'));
			console.log("json.status  "+json.status);
			console.log(json.status== 'success');
			if(json.hasOwnProperty('status') && (json.status== 'success')){
				console.log("in 1");
				copy.find('#document_template_id').val(tempname);
				console.log("json.hasOwnProperty('outputFormat') "+json.hasOwnProperty('outputFormat'));
				if(json.hasOwnProperty('outputFormat')){
					console.log("json.outputFormat "+json.outputFormat);
					copy.find('#document_outputFormat').val(json.outputFormat);
				}
				console.log("json.hasOwnProperty('static')  "+json.hasOwnProperty('static'));
				if(json.hasOwnProperty('static') && (json.static =='true')){
					console.log(json.static );
					console.log(document.getElementById(staticDynamicDoc).value);
					document.getElementById(staticDynamicDoc).checked = true;
				}
				if(json.hasOwnProperty('dynamic') && (json.dynamic =='true')){
					console.log("json.dynamic  "+json.dynamic);
					console.log();
					document.getElementById(dynamicDynamicDoc).checked = true;
				}
				if(json.hasOwnProperty('selectedTemplate')){
					console.log("json.selectedTemplate "+json.selectedTemplate );
					copy.find('#mySelecttemp').val(json.selectedTemplate)=json.selectedTemplate;
				}
				if(json.hasOwnProperty('selectedRule')){
					console.log("json.selectedRule  "+json.selectedRule);
					copy.find('#ruleforDoc').val(json.selectedRule);
				}
				if(json.hasOwnProperty('Ruledata')){
					console.log();
					$("#ruleforDoc option:selected").attr("data-ruledata");
				}
				if(json.hasOwnProperty('advanced')){
					
				}
				if(json.hasOwnProperty('selectedController')){
					console.log("json.selectedController "+json.selectedController);
					copy.find('#controllerforDoc').val(json.selectedController);
				}
				if(json.hasOwnProperty('selectedWorkflow')){
					copy.find('#workflowforDoc').val(json.selectedWorkflow);
				} 
	 	    }
		}
	});
}
      
function addApproverDropDown( divid,  idforselec){
	/* http://35.221.183.246:8082/portal/servlet/service/GetController?Email=doctiger@xyz.com&SFEmail=doctiger8@gmail.com
	Output: {"status":"success","Controller Name":["Controller1","Controlle2","Controlle"]}*/	
	var AllDiv = document.getElementsByClassName(divid);
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetApproversList?Email='+Email+'&group='+group,
		success: function (dataa) {
			var json = JSON.parse(dataa);
			var Approvers = json.Approvers;
			console.log(Approvers);
	    	for(var i=0; i<AllDiv.length ; i++){
	    		AllDiv[i].innerHTML = "";
	    		var myDiv= AllDiv[i];
	    	    var selectList = document.createElement("select");
	    	    selectList.id = idforselec;
	    	    selectList.classList.add("form-control");
	    	    selectList.classList.add("bg-gray");
	    	    selectList.classList.add("selectwkapproverclass");

	    	    myDiv.appendChild(selectList);
	    	    $("#"+idforselec).selectpicker();
	    	    var opt = document.createElement("option");
	    	    opt.text = "Select Approver";
	    	    selectList.appendChild(opt);
	    	    $("#"+idforselec).selectpicker("refresh");
	    	    for (var j = 0; j < Approvers.length; j++) {
	    	    	var option = document.createElement("option");
	    	    	option.value = Approvers[j];
	    	    	option.text = Approvers[j];
	    	    	selectList.appendChild(option);
	    	    	$("#"+idforselec).selectpicker("refresh");
	    	    }
	    	}
	    }
	});
}

$('#workflowapproverssave').click(function () {
//	 API: http://35.221.183.246:8082/portal/servlet/service/addWFApprover
//{"Email":"doctiger@xyz.com", "SFEmail":"doctiger8@gmail.com", "workflowType":"Clause","saveType":"new", "Approvers":["Approver1", "Approver2"]}
	var $this = $(this);  
	var copy = $(this).parents('.workflowaddMoreTextboxMain');
	var jsonData = {};
	var Approvers=[];

	jsonData["Email"]= Email;
	jsonData["group"]= group;
	jsonData["SFEmail"]= Email;
	jsonData["saveType"]= copy.find('#WorkflowApproverType').val();

	var workflowType=copy.find('#approverworkflowType').val();
	jsonData["workflowType"]=workflowType;
	var allapp= document.getElementsByClassName("selectwkapproverclass");
	console.log("allapp.length  "+allapp.length);
	for(var i=1; i<allapp.length-1; i++){
		Approvers.push(allapp[i].value);
	}
	jsonData["Approvers"]=Approvers;
	console.log(JSON.stringify(jsonData));
	$.ajax({
		type: 'POST',
		url: '/portal/servlet/service/addWFApprover',
		async:true,
		data:JSON.stringify(jsonData),
		dataType: 'json',
		contentType: 'application/json',
		success: function (dataa) {
			console.log("dataa    "+JSON.stringify(dataa));
			if(dataa.status=="success"){
				console.log("in success");
		 		window.location.reload();
		 	}else if(dataa.status=="error"){
		 		console.log("in error");
		 		document.getElementById('wkapprovererrormessage').innerHTML =dataa.message;
		 	}
		}
	}); 
});
 
function setprimaryOfEvent(eventname, eventid, inputfieldid, sffieldid ){
	document.getElementById('req-res-error1').innerHTML ="";

	var jsonData={};
	jsonData["email"]=Email;
	jsonData["group"]=group;
	jsonData["EventName"]=eventname;
	jsonData["EventId"]=eventid;

	$.ajax({
		type: 'POST',
		url: '/portal/servlet/service/getPrimarykey_excel.primarykey',
		async:true,
		data:JSON.stringify(jsonData),
		dataType: 'json',
		contentType: 'application/json',
		success: function (dataa) {
		   console.log("dataa    "+JSON.stringify(dataa));
		   document.getElementById(inputfieldid).value=dataa.data;
		   document.getElementById(sffieldid).value="excel";
	   }
	});	
}
 
function showeventlist(divid, idforselec){
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetEventList?email=' + Email + '&SFEmail=' + SFEmail+'&group='+group,
		success: function (dataa) {
			var json = JSON.parse(dataa);
			var EventList = json.EventList;
	   	    console.log(EventList);
	   	    var myDiv = document.getElementById(divid);
	   	    var selectList = document.createElement("select");
	   	    selectList.id = idforselec;
	   	    selectList.classList.add("form-control");
	   	    selectList.classList.add("bg-gray");
	   	    selectList.classList.add("selectpicker");
	   	    
	   	    myDiv.appendChild(selectList);
	   	    $("#"+idforselec).selectpicker();
	   	    var opt = document.createElement("option");
	   	    opt.text = "Select Event";
	   	    selectList.appendChild(opt);
	   	    $("#"+idforselec).selectpicker("refresh");
	   	    for (var i = 0; i < EventList.length; i++) {
	   	    	var option = document.createElement("option");
	   	    	option.value = EventList[i].EventName;
	   	    	option.text = EventList[i].EventName;
	   	    	option.setAttribute("EventId", EventList[i].EventId);

	   	    	selectList.appendChild(option);
	   	    	$("#"+idforselec).selectpicker("refresh");
	   	    }
		}
	});
}
 
$('#req-res-int-event1-save').click(function () {	 
//	API: - http://35.201.178.201:8082/portal/servlet/service/SaveRequestResponse
//		Input: {"Email":"user@gmail.com", "eventId":"0","selectedEvent":"Event1", "primaryKey":"AccountId","SFObject":"Account", "outputfiledname":"response"}
	var $this = $(this);
	var copy = $(this).parents('#requestResponse');
	var jsonData = {};
	var Approvers=[];

	jsonData["Email"]= Email;
	jsonData["group"]= group;
	jsonData["SFEmail"]= Email;
		 
	var eventname = copy.find('#req-res-int-sub-event-id1').val();  
	var eventid =copy.find('#req-res-int-sub-event-id1').find('option:selected').attr("eventid");
	var primaryKey =copy.find('#event-pk-id1').val();
	var outputfiledname =copy.find('#event1-outputfield').val();

	jsonData["eventId"]= eventid;
	jsonData["selectedEvent"]= eventname;
	jsonData["primaryKey"]= primaryKey;
	jsonData["SFObject"]= "";
	jsonData["outputfiledname"]= outputfiledname;
	
	console.log("jsonData  "+JSON.stringify(jsonData));

	 $.ajax({
		 type: 'POST',
		 url: '/portal/servlet/service/SaveRequestResponse',
		 async:true,
		 data:JSON.stringify(jsonData),
		 dataType: 'json',
		 contentType: 'application/json',
		 success: function (dataa) {
			 console.log("dataa    "+JSON.stringify(dataa));
			 if(dataa.status=="success"){
				 console.log("in success");
		 		 window.location.reload();
			 }else if(dataa.status=="error"){
				 console.log("in error");
				 document.getElementById('req-res-error1').innerHTML =dataa.status;
			 }
		}
	 });	 
});
 
$('#req-res-DocGen-event2-save').click(function () {
	document.getElementById('req-res-error1').innerHTML ="";
//	API: - http://35.201.178.201:8082/portal/servlet/service/SaveRequestResponse
//		Input: {"Email":"user@gmail.com", "eventId":"0","selectedEvent":"Event1", "primaryKey":"AccountId","SFObject":"Account", "outputfiledname":"response"}
	var $this = $(this);  
	var copy = $(this).parents('#requestResponse');
		
	var jsonDatapk = {};
	var jsonDataDD = {};
	var excelData={};
	var Approvers=[];
	var eventname = copy.find('#req-res-int-sub-event-id2').val();  
	var eventid =copy.find('#req-res-int-sub-event-id2').find('option:selected').attr("eventid");
	var primaryKey =copy.find('#event-pk-id2').val();
	var primaryKey_value =copy.find('#event2-pkvalue').val();	    
	
	jsonDatapk["email"]= Email;
	jsonDatapk["group"]= group;
	jsonDatapk["SFEmail"]= Email;
	jsonDatapk["EventName"]= eventname;
	jsonDatapk["EventId"]= eventid;
	jsonDatapk["Primary_key"]= primaryKey;
	jsonDatapk["Primary_key_value"]= primaryKey_value;   
	
	jsonDataDD["Email"]= Email;
	jsonDataDD["group"]= group;
	jsonDataDD["EventId"]= eventid;
	jsonDataDD["EventName"]= eventname;
	jsonDataDD["Primery_key"]= primaryKey;
	jsonDataDD["SFObject"]= "";
	jsonDataDD["Primery_key_value"]= primaryKey_value;
	
	console.log("jsonDatapk  "+JSON.stringify(jsonDatapk));

	$.ajax({
		type: 'POST',
		url: '/portal/servlet/service/getPrimarykey_excel.exceldata',
		async: false,
		data:JSON.stringify(jsonDatapk),
		dataType: 'json', 
		contentType: 'application/json',
		success: function (dataa) {
			console.log("dataa    "+JSON.stringify(dataa));
			console.log("*************************excel    "+JSON.stringify(dataa));
			excelData=dataa;
			jsonDataDD["SFData"]= dataa;		
		}
	});
	console.log("jsonDataDD  "+JSON.stringify(jsonDataDD));
	$.ajax({
		type: 'POST',
		url: '/portal/servlet/service/dDependency_core',
//		url: '/portal/servlet/service/dDependency_core_comment',
		async:false,
		data:JSON.stringify(jsonDataDD),
		contentType: 'application/json',
		dataType: 'text',
		success: function (dataa) {
			console.log("************************* Data12   "+dataa);
//			console.log("************************* DD   "+JSON.stringify(dataa));
			var atag='<a href="'+dataa+'">click here to view file</a>';
			document.getElementById("genDocLink").innerHTML=atag;
		}
	 });
});
 
//pallavi --communicationtwo  ==========
function addattachtemplatecomm(selectclass){
	
	//alert("group  "+group);
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetDTATemplateList?email=' + Email + '&SFEmail=' + SFEmail+'&group='+group,
		async: true,
		success: function (dataa) {
			
				var json = JSON.parse(dataa);
		    	var TemplatesList = json.Templates;
		    	console.log(TemplatesList);
		    	var x = document.getElementsByClassName(selectclass);
		    	//alert(x.length);
		    	var eleId;
		    	for(var i=0; i<x.length; i++){
		    		console.log("i= "+i)
		    		if(x[i].tagName=== 'SELECT' || x[i].tagName=== 'select'){
		    			//alert(x[i].innerHTML);
		    			console.log(x[i]);
		    			eleId= x[i].getAttribute( 'id' );
		    			console.log(eleId);
		    		x[i].innerHTML="";
		    		x[i].options[x[i].options.length] = new Option("Select Template", "Select Template");
		    		for(var j=0; j<TemplatesList.length; j++){
		    			console.log(x[i]);
		    			var key =TemplatesList[j].templatename;
		    			var value= TemplatesList[j].templatetype;
		    			x[i].options[x[i].options.length] = new Option(key, value);
		    			console.log(eleId);
		    			$("#"+eleId).selectpicker("refresh");
		    			
		    		}	    		
		    	}else{
					
				}			    	
		}}
	});
}

function addmailtemplatecomm(selectclass){
	//alert("group "+group);
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetDTAMailTempList?email=' + Email + '&SFEmail=' + SFEmail+'&group='+group,
		async: true,
		success: function (dataa) {
			console.log(dataa);
			try{
				var json = JSON.parse(dataa);
				var MailTemplates = json.MailTemplates;
				console.log(MailTemplates);
				var x = document.getElementsByClassName(selectclass);
				var eleId;
				for(var i=0; i<x.length; i++){
					if(x[i].tagName=== 'SELECT' || x[i].tagName=== 'select'){
						eleId= x[i].getAttribute( 'id' );
					x[i].innerHTML="";
					x[i].options[x[i].options.length] = new Option("Select Mail Template", "Select Mail Template");
					for(var j=0; j<MailTemplates.length; j++){
						var key =MailTemplates[j];
						var value= MailTemplates[j];
						x[i].options[x[i].options.length] = new Option(key, value);
						$("#"+eleId).selectpicker("refresh");
			}
				}}
				}catch(err){
					console.log(err);
			}
		}
	});
}

function addSMStemplatecomm(selectclass){
	//alert("group "+group);
	$.ajax({
		type: 'GET',
		url: '/portal/servlet/service/GetDTASMSTempListD?email=' + Email + '&SFEmail=' + SFEmail+'&group='+group,
		async: true,
		success: function (dataa) {
			try{
				var json = JSON.parse(dataa);
				var SMSTemplates = json.SMSTemplates;
				console.log(SMSTemplates);
				var x = document.getElementsByClassName(selectclass);
				var eleId;
		    	
				for(var i=0; i<x.length; i++){
					if(x[i].tagName=== 'SELECT' || x[i].tagName=== 'select'){
						eleId= x[i].getAttribute( 'id' );
					
					x[i].innerHTML="";
					x[i].options[x[i].options.length] = new Option("Select SMS Template", "Select SMS Template");
					for(var j=0; j<SMSTemplates.length; j++){
						var key =SMSTemplates[j];
						var value= SMSTemplates[j];
						x[i].options[x[i].options.length] = new Option(key, value);
						$("#"+eleId).selectpicker("refresh");
					}
				}}
			}catch(err){
				console.log(err);
			}
		}
	});
}

/*$('.savenewevent-comm').click(function () {
	{"Email":"doctiger@xyz.com", "savetype":"new", "eventname":"EN01", "SendToCMS":"true", 
	 * "Data":[{"CT":"mail,sms,download", "MailTempName":"Temp3", "AttachedTempName":"TestTemp","AttachedTempType":"",
	 * "SMSTempName":"SMS1"}]}  
	var mainjson={};
	var eventname=document.getElementById("new-event-name").value;
	var savetype=document.getElementById("comm-event-savetype").value;
	var SendToCMS="true";

	mainjson["Email"]=Email;
	mainjson["savetype"]=savetype;
	mainjson["eventname"]=eventname;
	mainjson["SendToCMS"]=SendToCMS;

	var alldataarr=[];
	var attachtypearray=[];
	var eachcount=0;
	$("#eventnewmaindiv select").each(function() { 
		var log = "";
		$(this).find('option:selected').each(function() { 			
			log = log + $(this).text() + ",";
		});
		
		console.log("log  "+log);
		alldataarr.push(log);
		eachcount++;
		console.log("eachcount % 4 == 0  "+eachcount % 4 == 0)
		if(eachcount % 4==0){
			console.log("in %");
			attachtypearray.push($(this).val());
		}
		console.log(" attachtypearray "+attachtypearray);
	});	
	console.log("attachtypearray "+attachtypearray.length);
	for(var k=0; k<attachtypearray.length; k++){
		console.log("attach type array "+attachtypearray[k]);
	}
	var dataarray=[];
	var regex = new RegExp(',', 'g');
	var count=0;
	for(var i=0; i<alldataarr.length; i++){
		console.log("i"+i)
		var CTsubjson={};
		console.log(alldataarr[i]);
		var array = alldataarr[i].split(',');
		for(var j=0; j<array.length; j++){
			console.log(array[j]);
		}
		console.log(array.length);
		if(array[1]=="Mail" && array[2].replace(regex, '')=="SMS"){
			CTsubjson["MailTempName"]=alldataarr[i+1];
			CTsubjson["SMSTempName"]=alldataarr[i+2];			
			CTsubjson["AttachedTempName"]=alldataarr[i+3];
			CTsubjson["AttachedTempType"]=attachtypearray[count];
			var CT= array[1]+","+array[2].replace(regex, '');
			CTsubjson["CT"]=CT;
		}else if(array[1].replace(regex, '')=="Mail" ){
			CTsubjson["MailTempName"]=alldataarr[i+1];
			CTsubjson["AttachedTempName"]=alldataarr[i+2];
			CTsubjson["AttachedTempType"]=attachtypearray[count];
			var CT= array[1].replace(regex, '');
			CTsubjson["CT"]=CT;
		}else if(array[1].replace(regex, '')=="SMS" ){
			CTsubjson["SMSTempName"]=alldataarr[i+1];
			CTsubjson["AttachedTempName"]=alldataarr[i+2];
			CTsubjson["AttachedTempType"]=attachtypearray[count];
			var CT= array[1].replace(regex, '');
			CTsubjson["CT"]=CT;
		} 
		console.log("CTsubjson "+JSON.stringify(CTsubjson));
		dataarray.push(CTsubjson);
		i++;
		i++;
		i++;
		count++;
	}
	console.log("dataarray "+JSON.stringify(dataarray));
	   mainjson["Data"]=dataarray;
	   console.log("**mainjson    "+JSON.stringify(mainjson));
	   $.ajax({
		   type: 'POST',
		   url: '/portal/servlet/service/DTASaveEvent',
			   async: true,
			   data:JSON.stringify(mainjson),
			   dataType: 'json', 
			   contentType: 'application/json',
			   success: function (dataa) {
				   console.log("dataa    "+dataa);
				   console.log("*************************excel    "+JSON.stringify(dataa));
				   var status=dataa.status
				   if(status=="success"){
					  // window.location.reload();
				   }else if(status=="error"){
					   document.getElementById("eventnew-error").innerHTML=dataa.message;
				   }
			   }
	   });
});*/

$('.savenewevent-comm').click(function () {
	//alert("in save evenet");
	var mainjson={};
	var eventname=document.getElementById("new-event-name").value;

	var savetype=document.getElementById("comm-event-savetype").value;
	var SendToCMS="true";

	mainjson["Email"]=Email;
	mainjson["group"]=group;
	mainjson["savetype"]=savetype;
	mainjson["eventname"]=eventname;
	mainjson["SendToCMS"]=SendToCMS;

	var alldataarr=[];
	var attachtypearray=[];
	var eachcount=0;
	$("#eventnewmaindiv select").each(function() { 
	var log = "";

	$(this).find('option:selected').each(function() { 
	log = log + $(this).text() + ",";

	});
	console.log("log :: "+log);
	alldataarr.push(log);

	eachcount++;
	console.log("eachcount % 4 == 0 "+eachcount % 4 == 0)
	if(eachcount % 4==0){
	console.log("in %");
	attachtypearray.push($(this).val());
	}


	}); 
	console.log("attachtypearray "+attachtypearray.length);
	for(var k=0; k<attachtypearray.length; k++){
	console.log("attach type array "+attachtypearray[k]);
	}

	var dataarray=[];
	var regex = new RegExp(',', 'g');
	//replace via regex
	//str = str.replace(regex, '');
	var count=0;
	for(var i=0; i<alldataarr.length; i++){
	console.log("i"+i)
	var CTsubjson={};
	console.log(alldataarr[i]);
	var array = alldataarr[i].split(',');
	for(var j=0; j<array.length; j++){
	console.log(array[j]);
	}
	console.log(array.length);
	if(array[1]=="Mail" && array[2].replace(regex, '')=="SMS"){
	CTsubjson["MailTempName"]=alldataarr[i+1].replace(regex, '');
	CTsubjson["SMSTempName"]=alldataarr[i+2].replace(regex, ''); 
	CTsubjson["AttachedTempName"]=alldataarr[i+3].replace(regex, '');
	CTsubjson["AttachedTempType"]=attachtypearray[count];
	var CT= array[1]+","+array[2].replace(regex, '');
	CTsubjson["CT"]=CT;
	//&& array.length==2
	}else if(array[1].replace(regex, '')=="Mail" ){
	CTsubjson["MailTempName"]=alldataarr[i+1].replace(regex, '');
	CTsubjson["AttachedTempName"]=alldataarr[i+3].replace(regex, '');
	CTsubjson["AttachedTempType"]=attachtypearray[count];

	var CT= array[1].replace(regex, '');
	CTsubjson["CT"]=CT;
	//&& array.length==2
	}else if(array[1].replace(regex, '')=="SMS" ){

	CTsubjson["SMSTempName"]=alldataarr[i+1].replace(regex, '');
	CTsubjson["AttachedTempName"]=alldataarr[i+2].replace(regex, '');
	CTsubjson["AttachedTempType"]=attachtypearray[count];
	var CT= array[1].replace(regex, '');
	CTsubjson["CT"]=CT;
	} 
	console.log("CTsubjson "+JSON.stringify(CTsubjson));
	dataarray.push(CTsubjson);
	i++;
	i++;
	i++;
	count++;
	}
	console.log("dataarray "+JSON.stringify(dataarray));

	mainjson["Data"]=dataarray;

	console.log("**mainjson "+JSON.stringify(mainjson));
	///call ajax to save event
	$.ajax({
	type: 'POST',
	url: '/portal/servlet/service/DTASaveEvent',
	async:false,
	data:JSON.stringify(mainjson),
	dataType: 'json', 
	contentType: 'application/json',
	success: function (dataa) {
	console.log("dataa "+dataa);
	alert("Event saved successfully");
	console.log("*************************excel "+JSON.stringify(dataa));
	window.location.reload();
	var status=dataa.status
	if(status=="success"){


	//window.location.reload();


	// $('#communicationTwo').show();
	// $('#recIntegration').hide();
	// $('#requestResponse').hide();

	// <li id="communicationsbutab" class="active"><a href="#communicationTwo" role="tab" data-toggle="tab">Communication</a></li>
	// $('#communicationsbutab-anchor').click();

	// alert("end of save event"); 
	}else if(status=="error"){

	document.getElementById("eventnew-error").innerHTML=dataa.message;

	}
	}

	});

	});

function addeventcomm(selectclass){
	//alert("in addeventcomm");
	// if there is no event it is giving error messahe handle tha and show only create new event option
	$.ajax({ 
	type: 'GET',
	url: '/portal/servlet/service/GetEventList?email=' + Email + '&SFEmail=' + SFEmail+'&group='+group,
	async:false,
	success: function (dataa) {
	//alert(dataa);
	var json = JSON.parse(dataa);
	var EventList = json.EventList;
	console.log(EventList);
	var selectHtml = document.getElementById("selectEvent-communication").innerHTML; 
	var option = "";
	for(var j=0; j<EventList.length; j++){
	
		var key =EventList[j].EventName;
		var value= EventList[j].EventId;
		option = option + "<option value="+value+">"+key+"</option>"
		//x[i].options[x[i].options.length] = new Option(key, value);
		//x[i].options.add( new Option(key,value) );
	}
	document.getElementById("selectEvent-communication").innerHTML = selectHtml + option;
//	var x = document.getElementsByClassName(selectclass);
//	//alert("x.length" +x.length);
//	for(var i=1; i<x.length; i++){
//	//alert("in for "+i);
//	x[i].innerHTML="";
//
//	x[i].options[x[i].options.length] = new Option("--Select Event--", "--Select Event--");
//	x[i].options[x[i].options.length] = new Option("Create New Event", "eventnew");
//
//	for(var j=0; j<EventList.length; j++){
//
//	var key =EventList[j].EventName;
//	var value= EventList[j].EventId;
//	x[i].options[x[i].options.length] = new Option(key, value);
//	//x[i].options.add( new Option(key,value) );
//	}
//
//	}
	}
	});
	}

//=================================================
var qr_tempName1;		
var qr_group;
$('.btn-QRcode').click(function(){
	console.log("In function");
	var popup= window.open("http://bluealgo.com:8082/portal/servlet/service/QRCodenLogo");
	popup.onload = function() {
        console.log("test");
    	qr_tempName1= document.getElementById("tempName").value;
        qr_group=document.getElementById("working-group-Dropdown-id").value;

    	console.log(qr_tempName1);
    	console.log(qr_group);

    	localStorage.setItem("tempName",qr_tempName1);
    	localStorage.setItem("qr_group", qr_group);
    	console.log("qr_tempName1 "+localStorage.getItem("tempName")+" qr_group "+qr_group);
    	//alert("qr_tempName1 "+qr_tempName1);
    } 
});

//down-para-btn
/*$("body").on("click",".down-para-btn",function(){
	console.log();
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/GetFileName?email=doctiger8@gmail.com',
		success:function(dataa){
			console.log(dataa);
			var json=JSON.parse(dataa);
			var filepath= json.FilePath;
			console.log(filepath);
			document.getElementById("imageid").src=filepath;
		}
	});
});
*/