/**
  1. 유저 프로파일 페이지
  (1) 유저 프로파일 페이지 구독하기, 구독취소
  (2) 구독자 정보 모달 보기
  (3) 구독자 정보 모달에서 구독하기, 구독취소
  (4) 유저 프로필 사진 변경
  (5) 사용자 정보 메뉴 열기 닫기
  (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
  (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달 
  (8) 구독자 정보 모달 닫기
 */

// (1) 유저 프로파일 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId, obj) {
  if ($(obj).text() === "구독취소") {
    $.ajax({
      type: "delete",
      url: "/api/subscribe/" + toUserId,
      dataType: "json"
    }).done(res => {
      $(obj).text("구독하기");
      $(obj).toggleClass("blue");
    }).fail(error => {
      console.log("구독취소실패", error);
    });
  } else {
    $.ajax({
      type: "post",
      url: "/api/subscribe/" + toUserId,
      dataType: "json"
    }).done(res => {
      $(obj).text("구독취소");
      $(obj).toggleClass("blue");
    }).fail(error => {
      console.log("구독하기실패", error);
    });
  }
}

// (2) 구독자 정보  모달 보기
function subscribeInfoModalOpen(pageUserId) {
  $(".modal-subscribe").css("display", "flex");

  $.ajax({
    url: `/api/user/${pageUserId}/subscribe`, // subscribeDtos 리턴
    dataType: "json"
  }).done(res => { // res == subscribeDtos
    console.log(res.data);

    res.data.forEach((userInfo) => { // userInfo == subscribeDtos 중 1개 원소
      let item = getSubscribeModalItem(userInfo);
      $("#subscribeModalList").append(item);
    });
  }).fail(error => {
    console.log("구독정보 불러오기 실패", error);
  });
}

function getSubscribeModalItem(userInfo) {
  let item = `
  <div class="subscribe__item" id="subscribeModalItem-${userInfo.id}">
    <div class="subscribe__img">
      <img src="/upload/${userInfo.profileImageUrl}" onerror="this.src='/images/person.jpeg'" />
    </div>
    <div class="subscribe__text">
      <h2>${userInfo.username}</h2>
    </div>
    <div class="subscribe__btn">`;

  if (!userInfo.equalUserState) { // 동일 유저가 아닐 때
    if (userInfo.subscribeState) { // 구독한 상태
      item += `<button class="cta blue" onclick="toggleSubscribe(${userInfo.id}, this)">구독취소</button>`
    } else { // 구독 안 한 상태
      item += `<button class="cta" onclick="toggleSubscribe(${userInfo.id}, this)">구독하기</button>`
    }
  }

  item += `
    </div>
  </div>`;

  return item;
}


// (3) 구독자 정보 모달에서 구독하기, 구독취소
/*function toggleSubscribeModal(obj) {
  if ($(obj).text() === "구독취소") {
    $(obj).text("구독하기");
    $(obj).toggleClass("blue");
  } else {
    $(obj).text("구독취소");
    $(obj).toggleClass("blue");
  }
}*/

// (3) 유저 프로파일 사진 변경 (완)
function profileImageUpload(pageUserId, principalId) {

  if (pageUserId != principalId) {
    alert("프로필 사진을 수정할 권한이 없습니다.")
    return;
  }

  $("#userProfileImageInput").click();

  $("#userProfileImageInput").on("change", (e) => {
    let f = e.target.files[0];

    if (!f.type.match("image.*")) {
      alert("이미지를 등록해야 합니다.");
      return;
    }

    // 서버에 이미지 전송
    let userProfileImageForm = $("#userProfileImageForm")[0];

    // FormData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의 K-V 쌍을 담을 수 있다.
    let formData = new FormData(userProfileImageForm);

    $.ajax({ // 응답을 데이터로 받을 예정이므로 url에서 apiController호출
      type: "put",
      url: `/api/user/${principalId}/profileImageUrl`,
      data: formData,
      contentType: false, // 기본형 비활성화 -> Default is "application/x-www-form-urlencoded; charset=UTF-8"
      processData: false, // contentType이 false일 때 processData의 값이 QueryString(="a=bc&d=e%2Cf")으로 자동설정됨을 방지
      enctype: "multipart/form-data", // enctype을 <form>에서 줬으면 생략 가능
      dataType: "json" // 서버에서 응답을 json으로 받음
    }).done(res => {
      // 사진 전송 성공시 이미지 변경
      let reader = new FileReader();
      reader.onload = (e) => {
        $("#userProfileImage").attr("src", e.target.result);
      }
      reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
    }).fail(error => {
      console.log("오류",error)
    });
  });
}


// (4) 사용자 정보 메뉴 열기 닫기
function popup(obj) {
  $(obj).css("display", "flex");
}

function closePopup(obj) {
  $(obj).css("display", "none");
}


// (5) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
  $(".modal-info").css("display", "none");
}

// (6) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
  $(".modal-image").css("display", "none");
}

// (7) 구독자 정보 모달 닫기
function modalClose() {
  $(".modal-subscribe").css("display", "none");
  location.reload();
}






