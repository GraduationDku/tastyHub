import { useState } from 'react';

async function refreshAccessToken(){
  const refreshToken = document.cookie
  .split('; ')
  .find((row) => row.startsWith('refreshToken='))
  ?.split('=')[1];

  if (!refreshToken) {
    throw new Error('리프레시 토큰 없음')
  }

  const response= await fetch(`${process.env.REACT_APP_API_URL}/user/refresh`,{
    method: 'POST',
        headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ refreshToken }),
  });

  if (!response.ok){
    throw new Error("리프레시 갱신 실패");
  }

  const data = await response.json();
  return data.accessToken;
}

function useFetchWithToken(url, options){
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchData = async () => {
    setLoading(true);

    try {
      const response = await fetch(url, {
        ...options,
        headers: {
          ...options.headers,
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        },
      });

      if (response.status === 403) {
        const newAccessToken = await refreshAccessToken();
        localStorage.setItem('accessToken', newAccessToken);

        const retryResponse = await fetch(url, {
          ...options,
          headers: {
            ...options.headers,
            Authorization: `Bearer ${newAccessToken}`,
          },
        });

        const retryData = await retryResponse.json();
        setData(retryData);
      } else {
        const responseData = await response.json();
        setData(responseData);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return { data, error, loading, fetchData };
}

export default useFetchWithToken;
