# CTRLF_Android
![Ctrlf_Logo](/assets/Ctrlf_Logo_e5qwtcgu1.png)


# Contributor
<a href="https://github.com/ThinLineIT/CtrlF_Android/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=ThinLineIT/CtrlF_Android" />
</a>

Made with [contributors-img](https://contrib.rocks).

# 서비스 소개
개발자를 위한 CS 정보를 제공하는 모바일 어플리케이션 CtrlF
---
- 다양한 CS 정보를 확인하고, 더 나아가서는 직접 CS 정보를 만들어 공유하는 서비스입니다.
  *  CS 정보를 Note, Topic, Page 트리구조로 나눠 마크다운뷰로 제공합니다.
  * Issue와 Editor를 이용하여 CS 정보를 생성,수정,삭제가 가능합니다.

# 주요 기능
스플래시
```bash
앱 구동 시 CtrlF 애니메이션이 나타납니다.
```

로그인
```bash
사용자의 ID, PW를 입력받아 로그인합니다.
```

메인
```bash
현재 모아진 아이디어의 갯수, 노트리스트, 이슈리스트를 보여줍니다.
```

노트리스트
```bash
노트리스트를 보여주며, 노트 추가 버튼으로 노트 추가가 가능합니다.
```

노트디테일
```bash
 선택된 노트의 하위 토픽 리스트를 보여주며, 토픽 추가 버튼으로 토픽 추가가 가능합니다.
```

페이지리스트
```bash
선택된 토픽의 하위 페이지 리스트를 보여주며, 페이지 추가 버튼으로 페이지 추가가 가능합니다.
```

페이지디테일
```bash
선택된 페이지의 정보를 마크다운뷰로 보여주며, "관련이슈로 이동", "수정하기" 기능을 사용 가능합니다.
```

페이지에디터
```bash
제목, 수정사유, 내용 등을 채워 페이지 생성이 가능하며, 다양한 마크다운 기능 및 사진 추가, 마크다운 프리뷰을 지원합니다.
```

이슈리스트
```bash
현재 생성된 이슈리스트를 보여줍니다.
```

이슈디테일
```bash
선택된 이슈의 정보를 보여주며, 권한에 따라 이슈의 승인, 미승인, 수정, 삭제 등의 기능을 사용 가능합니다.
```

# Packaging
```bash
CtrlF
  ├─📂entity
  ├─📂issue
  │     ├─📂detail
  │     └─📂issue
  ├─📂main
  │     └─📂banner
  ├─📂note
  ├─📂page
  │     ├─📂detail
  │     └─📂editor
  ├─📂registration
  │     ├─📂signin
  │     ├─📂signout
  │     └─📂signup
  ├─📂repository
  │     ├─📂dao
  │     ├─📂dto
  │     │     ├─📂request
  │     │     └─📂response
  │     └─📂network
  │           └─📂api
  ├─📂splash
  └─📂util
        ├─📂base
        └─📂uri
```

# Specification
<table class="tg">
<tbody>
  <tr>
    <td><b>Architecture</b></td>
    <td>MVVM</td>
  </tr>
<tr>
    <td><b>Jetpack Components</b></td>
<td>DataBinding, LiveData, ViewModel, Lifecycle, viewPager2, Navigation </td>
</tr>
<tr>
    <td><b>Other Library</b></td>
<td>Glide, MarkdownView, SlidingPaneLayout </td>
</tr>
<tr>
    <td><b>Network</b></td>
<td>Retrofit2, coroutine</td>
</tr>
<tr>
    <td><b>Strategy</b></td>
<td>Git Flow</td>
</tr>
<tr>
    <td><b>Other Tool</b></td>
<td>Slack, Trello, Confluence</td>
</tr>


</tbody>
</table>
