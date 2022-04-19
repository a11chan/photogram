function update(userId, event) {
  event.preventDefault();
  
  let data = $("#profileUpdate").serialize();
  
  console.log(data);
  
  $.ajax({
    type:"put",
    url:`/api/user/${userId}`,
    data:data,
    contentType:"application/x-www-form-urlencoded; charset=utf-8",
    dataType:"json"
  }).done(res=>{ // http 상태코드가 200번대
    console.log("성공",res);
    location.href=`/user/${userId}`;
  }).fail(error=> { // http 상태코드가 200번대 아닐 때
    
    if(error.data == null) {
      alert(error.responseJSON.message);
    } else {
      alert(JSON.stringify(error.responseJSON.data)); /* JS객체를 JSON형식으로 변환 */
    }
  });
}