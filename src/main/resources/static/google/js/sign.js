function signinCallback(authResult) {
    if (authResult['access_token']) {
        // 승인 성공
        // 사용자가 승인되었으므로 로그인 버튼 숨김. 예:
        document.getElementById('signinButton').setAttribute('style', 'display: none');
        document.getElementById('signoutButton').setAttribute('style', 'display: inherit');
        // test
        console.log('success: ' + authResult['access_token']);
    } else if (authResult['error']) {
        // 오류가 발생했습니다.
        // 가능한 오류 코드:
        //   "access_denied" - 사용자가 앱에 대한 액세스 거부
        //   "immediate_failed" - 사용자가 자동으로 로그인할 수 없음
        // console.log('오류 발생: ' + authResult['error']);
        alert('로그인 실패: ' + authResult['error']);
    }
}