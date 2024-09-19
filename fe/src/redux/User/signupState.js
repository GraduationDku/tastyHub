import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

const initialState = {
  userName: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  email: '',
  verifiedCode: '',
  usernameAvailable: false,
  nicknameAvailable: false,
  emailAvailable: false,
  verificationSuccess: false,
  passwordsMatch: true,
  usernameButtonText: '아이디 확인',
  nicknameButtonText: '닉네임 확인',
  emailButtonText: '이메일 중복 확인',
  verifyButtonText: '인증번호 확인',
  signupSuccess: false,
  error: null,
};

// 비동기 함수
export const checkUsernameAvailability = createAsyncThunk(
  'signup/checkUsernameAvailability',
  async (userName, { rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user/overlap/userName?userName=${userName}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        return rejectWithValue(await response.text());
      }
      return { userName, available: true };
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const checkNicknameAvailability = createAsyncThunk(
  'signup/checkNicknameAvailability',
  async (nickname, { rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user/overlap/nickname?nickname=${nickname}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        return rejectWithValue(await response.text());
      }
      return { nickname, available: true };
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const sendVerificationCode = createAsyncThunk(
  'signup/sendVerificationCode',
  async (email, { rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/email`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });
      if (!response.ok) {
        return rejectWithValue(await response.text());
      }
      return email;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const verifyCode = createAsyncThunk(
  'signup/verifyCode',
  async ({ email, verifiedCode }, { rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/email/verified`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, verifiedCode }),
      });
      if (!response.ok) {
        return rejectWithValue(await response.text());
      }
      const result = await response.json();
      return result ? { email, success: true } : rejectWithValue('Verification failed');
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const signupUser = createAsyncThunk(
  'signup/signupUser',
  async ({ userName, password, nickname, email }, { rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user/signup`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userName,
          password,
          nickname,
          email,
        }),
      });
      if (!response.ok) {
        return rejectWithValue(await response.text());
      }
      return await response.json();
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);


const signupSlice = createSlice({
  name: 'signup',
  initialState,
  reducers: {
    updateField(state, action) {
      state[action.payload.field] = action.payload.value;
    },
    resetSignupState(state) {
      return initialState;
    },
    checkPasswordsMatch(state) {
      state.passwordsMatch = state.password === state.confirmPassword;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(checkUsernameAvailability.fulfilled, (state, action) => {
        state.usernameAvailable = action.payload.available;
        state.usernameButtonText = "확인되었습니다.";
      })
      .addCase(checkUsernameAvailability.rejected, (state, action) => {
        state.error = action.payload;
      })
      .addCase(checkNicknameAvailability.fulfilled, (state, action) => {
        state.nicknameAvailable = action.payload.available;
        state.nicknameButtonText = "확인되었습니다.";
      })
      .addCase(checkNicknameAvailability.rejected, (state, action) => {
        state.error = action.payload;
      })
      .addCase(sendVerificationCode.fulfilled, (state) => {
        state.emailButtonText = "발송되었습니다.";
      })
      .addCase(sendVerificationCode.rejected, (state, action) => {
        state.error = action.payload;
      })
      .addCase(verifyCode.fulfilled, (state, action) => {
        state.verificationSuccess = true;
        state.verifyButtonText = "확인되었습니다.";
        state.emailAvailable = true;
      })
      .addCase(verifyCode.rejected, (state, action) => {
        state.error = action.payload;
      })
      .addCase(signupUser.fulfilled, (state) => {
        state.signupSuccess = true;
      })
      .addCase(signupUser.rejected, (state, action) => {
        state.error = action.payload;
      });
  },
});

export const { updateField, resetSignupState, checkPasswordsMatch } = signupSlice.actions;

export default signupSlice.reducer;
