//var Email="doctiger@xyz.com";
if(document.getElementById("email").value=="anonymous"){
	var Email= "viki@gmail.com";
}else{
	var Email= document.getElementById("email").value;
}
var SFEmail=document.getElementById("email").value;

var qrJson={};
$("body").on("click",".save-QR",function(){
	console.log("test");
	/*{"Email":"doctiger8@gmail.com","templatename":"Template1","size":"Small","position":"Top Left","param":[{"SFObject":"Account",
	 * "Field":"Id"},{"SFObject":"Account","Field":"Name"},{"SFObject":"Contact","Field":"ContatcNo"}]} */
	var templatename=document.getElementById("qr_tempName").value;
	var size=document.getElementById("selSize").value;
	var position=document.getElementById("selPosition").value;
	var tableNo=document.getElementById("table_No").value;
//	/var savetype= document.getElementById("tempSaveType").value;
	
	qrJson["Email"]=Email;
	qrJson["group"]=localStorage.getItem("qr_group");;

	qrJson["templatename"]=localStorage.getItem("tempName");
	qrJson["size"]=size;
	qrJson["position"]=position;
	qrJson["tableNo"]= tableNo;
	var paramArr= JSON.parse(document.getElementById("headerLiArr").value);
	console.log(paramArr);
	//qrJson["param"]=document.getElementById("headerLiArr").value;// insert array of header here
	var field;
	
	var param=[];
	//["Row_Id", "Name", "Value"]
	for(var i=0; i<paramArr.length; i++){
		var paramJson={};
		field= paramArr[i];
		console.log(field);
		paramJson["SFObject"]="";
		paramJson["Field"]=paramArr[i];	
		param.push(paramJson);
	}
	
	console.log(param);
	qrJson["param"]=param;
	//alert(JSON.stringify(tempmainJson));
	console.log(JSON.stringify(qrJson));
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/QRCodeGeneration',
		async:true,
		data:JSON.stringify(qrJson),
		contentType:"application/json",
		success:function(dataa){
			//alert(tempdata);
			console.log(dataa);
			var json=JSON.parse(dataa);			
		}
	});	
});

var excelJson={};
$(document).ready(function() {
	/*var x= excelHeaders;
	console.log(x);
	for(var i=0;i<x.length;i++){
		$(".box-left-temp-lib").append('<div class="list-part"><ul><li>'+x[i]+'</li></ul></div>');
		}*/
	var qr_tempName1=localStorage.getItem("tempName");
	var qr_group=localStorage.getItem("qr_group");

	document.getElementById("qr_tempName").value= qr_tempName1;
	console.log("qr_tempName1 "+qr_tempName1 +"qr_group "+qr_group)
	
	var json;
	var headers;
	console.log("test");
	excelJson["email"]=Email;
	excelJson["group"]=qr_group;

	//alert(JSON.stringify(tempmainJson));
	console.log(excelJson);
	$.ajax({
		type:'POST',
		url:'/portal/servlet/service/getHeaders_Excel',
		async:false,
		data:JSON.stringify(excelJson),
		contentType:"application/json",
		success:function(dataa){
			//alert(tempdata);
			console.log(dataa);
			json=JSON.parse(dataa);		
			headers= json.Headers;
			console.log(json.Headers[0]);
			for(var i=0;i<headers.length;i++){
				$(".box-left-qr-lib").append('<div class="list-part"><ul><li>'+headers[i]+'</li></ul></div>');
				}
		}
	});	
});

$("body").on("click",".box li",function(){
	$(this).toggleClass('selected');
});

$("body").on("click",".click-right-qr-lib",function(){
	var liArr=[];	
	$("#headerLi li.selected").each(function(){
		var liText=$(this).text();
		
		$(".box-right-qr-lib").append('<div class="list-part"><ul id="hList"><li>'+liText+'</li></ul></div>');
			liArr.push(liText);
			document.getElementById("headerLiArr").value=JSON.stringify(liArr);
			if($(this).parent("ul").parent("div").find("ul li").length==1){
				$(this).parent("ul").parent("div").remove();
			}else{
				$(this).remove();
			}
			console.log(liArr);
	});
});