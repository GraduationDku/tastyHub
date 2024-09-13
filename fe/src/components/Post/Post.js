// src/redux/postState.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

// 초기 상태 정의
const initialState = {
  posts: [],
  loading: false,
  error: null,
};

// 게시글 목록을 가져오는 비동기 작업 
export const fetchPosts = createAsyncThunk(
  'post/fetchPosts',
  async (_, { rejectWithValue }) => {
    try {
      const response = await fetch(`https://localhost:443/post/list`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        }
      });
      if (!response.ok) {
        throw new Error('Failed to fetch posts');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

// 게시글 삭제 비동기 작업
export const deletePosts = createAsyncThunk(
  'post/deletePosts',
  async (postIds, { rejectWithValue }) => {
    try {
      for (let postId of postIds) {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/post/delete/${postId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken')
          }
        });
        if (!response.ok) {
          throw new Error(`Failed to delete post with id ${postId}`);
        }
      }
      return postIds;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

const postSlice = createSlice({
  name: 'post',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      // Fetch posts
      .addCase(fetchPosts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPosts.fulfilled, (state, action) => {
        state.loading = false;
        state.posts = action.payload;
      })
      .addCase(fetchPosts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // Delete posts
      .addCase(deletePosts.fulfilled, (state, action) => {
        state.posts = state.posts.filter(post => !action.payload.includes(post.postId));
      })
      .addCase(deletePosts.rejected, (state, action) => {
        state.error = action.payload;
      });
  },
});

export default postSlice.reducer;
