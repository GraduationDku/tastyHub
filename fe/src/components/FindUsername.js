import React, { useState } from 'react';

function FindUsername({ setVisible }) {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [username, setUsername] = useState(null);

  const handleFindUsername = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/user/findUsername', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });

      const data = await response.json();
      if (response.ok) {
        setUsername(data.username);
        alert('귀하의 아이디는: ' + data.username + ' 입니다.');
      } else {
        alert('해당 이메일로 등록된 아이디가 없습니다.');
      }
    } catch (error) {
      console.error('아이디 찾기 오류:', error);
      alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
    setLoading(false);
  };

  return (
    <div>
      <h2>아이디 찾기</h2>
      <input
        type="email"
        placeholder="이메일 입력"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <button onClick={handleFindUsername} disabled={loading}>
        {loading ? '로딩 중...' : '아이디 찾기'}
      </button>
      <button onClick={() => setVisible(false)}>닫기</button>
    </div>
  );
}
export default FindUsername;