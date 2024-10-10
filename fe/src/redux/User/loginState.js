// src/redux/loginState.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

// 비동기 로그인 액션 생성
export const loginUser = createAsyncThunk(
  'login/loginUser',
  async ({ userName, password }, { rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userName, password }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        return rejectWithValue(errorData.message || '로그인 실패');
      }

      const authorization = response.headers.get('Authorization');

      const data = await response.json();
      const nickname = data.nickname;

      // 로컬 스토리지에 토큰과 닉네임 저장
      localStorage.setItem('accessToken', authorization);
      localStorage.setItem('nickname', nickname);

      return { authorization, nickname };
    } catch (error) {
      return rejectWithValue(error.message || '서버 오류');
    }
  }
);

// 초기 상태 설정
const initialState = {
  user: localStorage.getItem('nickname') ? { nickname: localStorage.getItem('nickname') } : null,
  isAuthenticated: !!localStorage.getItem('accessToken'),
  loading: false,
  error: null,
};

// 슬라이스 생성
const loginSlice = createSlice({
  name: 'login',
  initialState,
  reducers: {
    logout: (state) => {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('nickname');
      state.user = null;
      state.isAuthenticated = false;
      state.loading = false;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.loading = false;
        state.isAuthenticated = true;
        state.user = {
          nickname: action.payload.nickname,
        };
        state.error = null;
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.loading = false;
        state.isAuthenticated = false;
        state.user = null;
        state.error = action.payload || '로그인 실패';
      });
  },
});

export const { logout } = loginSlice.actions;

export default loginSlice.reducer;
