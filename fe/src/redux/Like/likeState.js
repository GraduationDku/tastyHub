// redux/likeState.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export const fetchLikeCount = createAsyncThunk(
  'like/fetchLikeCount',
  async (recipeId, { rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/like/count/${recipeId}`, {
        headers: {
          'Authorization': localStorage.getItem('accessToken')
        }
      });

      if (!response.ok) {
        return rejectWithValue('Failed to fetch like count');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const toggleLike = createAsyncThunk(
  'like/toggleLike',
  async (recipeId, { dispatch, rejectWithValue }) => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/like/${recipeId}`, {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        return rejectWithValue('Error toggling like status');
      }

      // After toggling, fetch the updated like count
      dispatch(fetchLikeCount(recipeId));
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

const likeState = createSlice({
  name: 'like',
  initialState: {
    likeCount: 0,
    liked: false,
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchLikeCount.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchLikeCount.fulfilled, (state, action) => {
        state.likeCount = action.payload.count;
        state.liked = action.payload.liked;
        state.loading = false;
      })
      .addCase(fetchLikeCount.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(toggleLike.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(toggleLike.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(toggleLike.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  }
});

export default likeState.reducer;
