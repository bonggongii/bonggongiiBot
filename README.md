# 직원을 위한 정부기관 방문시민 종합 정보제공 플랫폼 개발

> 개발 기간 : 2024.09.29 ~ 2024.12.01
<br>

## 팀 소개

<table width="500" align="center">
<tbody>
<tr>
<th>Pictures</th>
<td width="100" align="center">
<img src="https://github.com/user-attachments/assets/f7c90ac4-0e04-48bb-a0d1-0af9b82f8dd6" width="80" height="80">

</td>
<td width="100" align="center">
<img src="https://github.com/user-attachments/assets/4c6d35eb-22b9-4aaf-bc72-12688da46720" width="100" height="100">

</td>
<td width="100" align="center">
<img src="https://github.com/user-attachments/assets/a3ccc0ce-0dc0-4c52-a969-caeba8e5f47f" width="100" height="100">

</td>
<td width="100" align="center">
<img src="https://github.com/user-attachments/assets/9bf139ac-8a74-4fdf-a26c-9f70e60294fe" width="100" height="100">

</td>
<td width="100" align="center">
<img src="https://github.com/user-attachments/assets/ee2fa919-dcbf-407c-ac7a-f1c47be4a3c5" width="100" height="100">

</td>
</tr>
<tr>
<th>Name</th>
<td width="100" align="center">이다은</td>
<td width="100" align="center">박수빈</td>
<td width="100" align="center">여언주</td>
<td width="100" align="center">이채은</td>
<td width="100" align="center">임현정</td>

</tr>
<tr>
<th>Role</th>
<td width="150" align="center">
- 앱 기획<br>
- UI 디자인<br>
- 화면 구현<br>
</td>
<td width="150" align="center">
- API 에러 처리 로직 구현<br>
- Git 관리<br>
</td>
<td width="150" align="center">
- 프로젝트 Gradle 설정 관리<br>
- OpenAI API 연동<br>
</td>
<td width="150" align="center">
- OpenAI 챗봇 모델 구축<br>
- 초기화, 로딩 기능 구현<br>
</td>
<td width="150" align="center">
- MVP 아키텍처 패턴 적용<br>
- streaming 응답 처리 로직 구현<br>
<br>
</td>
</tr>
<tr>
<th>GitHub</th>
<td width="100" align="center">
<a href="https://github.com/Dhani5703">
<img src="http://img.shields.io/badge/Dhani5703-green?style=social&logo=github"/>
</a>
</td>
<td width="100" align="center">
<a href="https://github.com/Soobin-Park">
<img src="http://img.shields.io/badge/SoobinPark-green?style=social&logo=github"/>
</a>
</td>
<td width="100" align="center">
<a href="https://github.com/eejj357">
<img src="http://img.shields.io/badge/eejj357-green?style=social&logo=github"/>
</a>
</td>
<td width="100" align="center">
<a href="https://github.com/Chae-eun-Lee">
<img src="http://img.shields.io/badge/ChaeeunLee-green?style=social&logo=github"/>
</a>
</td>
<td width="100" align="center">
<a href="https://github.com/HJunng">
<img src="http://img.shields.io/badge/HJunng-green?style=social&logo=github"/>
</a>
</td>
</tr>
</tbody>
</table>
<br>

### 🏆수상 | 경기도일자리재단『 커리어 부트캠프 』, **최우수상**
<div style="display: flex;">
  <img src="https://github.com/user-attachments/assets/d526d495-c247-4479-9fa0-840adf807f16" width="500" height="300" style="margin-right: 50px;">
  <img src="https://github.com/user-attachments/assets/e319a9b6-8f4f-42ba-95fd-e28506ddd339" width="500" height="300">
</div>
<br><br>


## 프로젝트 소개

### 🔍 프로젝트 개요
#### ▶️ 정부 기관(플랫폼)을 방문한 시민이 정보를 얻는 기존 방법
![image](https://github.com/user-attachments/assets/6a7009ef-abed-4a0f-a6f0-6268d42d4cf2)

#### ⏩️ 기대 효과
![image](https://github.com/user-attachments/assets/15506373-2924-4a89-8832-4580938dbc8a)


### 🔍 기술 스택
<img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">

<img src="https://img.shields.io/badge/android-34A853?style=for-the-badge&logo=android&logoColor=white">
<img src="https://img.shields.io/badge/android studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white">
<img src="https://img.shields.io/badge/retrofit-4EBF7F?style=for-the-badge&logo=android&logoColor=white">

<img src="https://img.shields.io/badge/openai-412991?style=for-the-badge&logo=openai&logoColor=white">

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">




## 주요 기능
### ✨ 챗봇 UI 화면 구성
- 사용자가 직관적으로 챗봇에게 질문을 입력하고 선택할 수 있는 인터페이스를 제공합니다.
- **질문 입력 창과 버튼**을 통해 사용자는 텍스트 입력 또는 **예시 질문 카드 선택**을 통해 질문을 제출할 수 있습니다.
- **RecyclerView**를 활용하여 채팅 화면을 구성하였으며, ****사용자 질문과 챗봇 응답이 시간순으로 정렬됩니다. **메시지 전송 후 자동 스크롤, 긴 답변 토글**(더보기, 접기)**, Streaming** 방식 적용 등을 통해 원활한 사용자 경험을 제공합니다.

###  💬 GPT API 연동 - Assistant API 활용
- OpenAI의 **Assistant API**를 활용하여 GPT 모델과 연결됩니다.
- 경기도 일자리재단 및 다양한 공공기관의 데이터셋을 **Vector Store**에 저장하고,  관련 데이터를 찾아 GPT가 신뢰도 높은 응답을 제공합니다.
- 대화 흐름은 **Thread와 Message 단위**로 관리되며, 각 Thread에서 다중 메시지를 주고받을 수 있습니다.
- Retrofit 및 OkHttp를 이용하여 API와의 원활한 통신을 구현하였습니다.

## 데모 영상 
<div align="center">
<video src = "https://github.com/user-attachments/assets/d3c3faca-ae19-41b8-a4b3-d6dba97a97a1" controls></video>
</div>


## 세부 기획
### 🎨 피그마
#### [🔗피그마 전체보기](https://www.figma.com/design/YuPBHBUsw1PCUCfnvkABDC/경기도-커리어부트-봉공이-AI챗봇?node-id=1-2244&node-type=frame&t=HZ6zAVXRci6p2C2U-0)
![image](https://github.com/user-attachments/assets/aef033f6-3acb-4c2b-821a-69573ea71484)

### 📖 요구사항 명세서
#### [🔗요구사항 명세서 및 피처리스트 전체보기](https://docs.google.com/spreadsheets/d/1vbSj1u1r4l2oXHQtlO7By4mJc5tXbMCyDQG7Z2i1p1I/edit?gid=818272064#gid=818272064)
![image](https://github.com/user-attachments/assets/e0783ea3-1900-4729-b785-74f2cc8a994d)

### ⚙️ 피처리스트
![image](https://github.com/user-attachments/assets/b787e155-71de-4a00-92c8-1eedd87377b6)

</div>
