console.log("boardComment.js in");
console.log(bnoValue);

document.getElementById("cmtAddBtn").addEventListener("click", () => {
  const cmtText = document.getElementById("cmtText");
  const cmtWriter = document.getElementById("cmtWriter");

  if (cmtText == null || cmtText.value == "") {
    alert("댓글 입력");
    cmtText.focus();
    return false;
  }
  let cmtData = {
    bno: bnoValue,
    writer: cmtWriter.innerText,
    content: cmtText.value,
  };
  //   전송
  postCommentToServer(cmtData).then((result) => {
    if (result == "1") {
      alert("등록 성공");
      // 다시 입력할 수 있도록 댓글 창을 비우고, 포커스 맞추기
      cmtText.value = "";
      cmtText.focus();
    }
    // 댓글 출력 호출
    spreadCommentList(bnoValue);
  });
});

// 화면에 출력하는 함수
// 만약에 페이지가 안들어오면 옵셔널 1
function spreadCommentList(bno) {
  commentListFromServer(bno).then((result) => {
    console.log(result);
    const ul = document.getElementById("cmtListArea");
    if (result.list.length > 0) {
      // 댓글이 있을 경우

      let li = "";
      for (let comment of result.list) {
        li += `<li class="list-group-item" data-cno=${comment.cno}>`;
        li += `<div class="ms-2 me-auto">`;
        li += `<div class="fw-bold">${comment.writer}</div>`;
        li += `${comment.content}`;
        li += `</div>`;
        li += `<span class="badge text-bg-primary">${comment.regDate}</span>`;
//        if(loginUser == comment.writer){
          li += `<button type="button" class="btn btn-sm btn-outline-warning mod" data-bs-toggle="modal" data-bs-target="#commentModal">e</button>`;
          li += `<button type="button" class="btn btn-sm btn-outline-danger del">x</button>`;
//        }
        li += `</li>`;
      }
      ul.innerHTML += li;
    } else {
      // 댓글이 없을 경우
      ul.innerHTML = `<li class="list-group-item">Comment List Empty</li>`;
    }
  });
}

document.addEventListener("click", (e) => {
  if (e.target.id == "moreBtn") {
    // 더보기 버튼 => 남아있는 게시글 5개를 더 출력 => 비동기 호출
    spreadCommentList(bnoValue, parseInt(e.target.dataset.page));
  }
  if (e.target.classList.contains("mod")) {
    // 수정 버튼 : 수정할 데이터(content, writer)를 찾아서 => modal 창에 띄우기
    // nextSibling : 같은 부모의 다음 형제 찾기
    let li = e.target.closest("li"); // 내 버튼이 속해있는 li 가져오기

    //return nextNode => nodeValue 텍스트만 분리
    let cmtText = li.querySelector(".fw-bold").nextSibling;
    let cmtWriter = li.querySelector(".fw-bold").innerText;
    let cno = li.dataset.cno;

    document.getElementById(
      "cmtWriterMod"
    ).innerHTML = `no.${cno} <b>${cmtWriter}</b>`;
    document.getElementById("cmtTextMod").value = cmtText.nodeValue;

    // cmtModBtn => data-cno="" 속성 추가
    document.getElementById("cmtModBtn").setAttribute("data-cno", cno);
  }
  if (e.target.id == "cmtModBtn") {
    // 모달 수정 버튼
    // 모달 수정 버튼을 클릭하면 => 수정 (비동기 처리)
    // cno, content => 서버로 전송 => update
    let modData = {
      cno: e.target.dataset.cno,
      content: document.getElementById("cmtTextMod").value,
    };
    // 비동기 함수 만들어서 전송
    updateCommentToServer(modData).then((result) => {
      if (result == "1") {
        alert("수정 성공");
      }
      // 변경 댓글 출력
      spreadCommentList(bnoValue);

      // 모달창 닫기
      document.querySelector(".btn-close").click();
    });
  }
  if (e.target.classList.contains("del")) {
    // 삭제 버튼
    let li = e.target.closest("li");
    removeCommentToServer(li.dataset.cno).then((result) => {
      if (result == 1) {
        alert("삭제 성공");
      } else {
        alert("삭제 실패");
      }
      spreadCommentList(bnoValue);
    });
  }
});

// ------------- 비동기 데이터 함수 --------------
// modify
async function updateCommentToServer(modData) {
  try {
    const url = "/comment/modify";
    const config = {
      method: "put",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        [csrfHeader] : csrfToken
      },
      body: JSON.stringify(modData),
    };
    const resp = await fetch(url, config);
    const result = await resp.text();
    return result;
  } catch (error) {}
}

// list
async function commentListFromServer(bno) {
  try {
    const resp = await fetch("/comment/list/" + bno);
    const result = await resp.json();
    return result;
  } catch (error) {
    console.log(error);
  }
}

// post
async function postCommentToServer(cmtData) {
  try {
    const url = "/comment/post";
    const config = {
      method: "post",
      headers: {
        "content-type": "application/json; charset=utf-8",
      },
      body: JSON.stringify(cmtData),
    };
    const resp = await fetch(url, config);
    const result = await resp.text();
    return result;
  } catch (error) {
    console.log(error);
  }
}

// remove
async function removeCommentToServer(cno) {
  try {
    const url = `/comment/remove/${cno}`;
    const config = {
      method: "delete",
      header:{
        [csrfHeader] : csrfToken
      }
    };

    const resp = await fetch(url, config);
    const result = await resp.text();
    return result;
  } catch (error) {
    console.log(error);
  }
}
