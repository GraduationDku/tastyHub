// state들 보관하는 파일
import { configureStore } from '@reduxjs/toolkit'
import likeReducer from './redux/Like/likeState';

export default configureStore({
	reducer: {
		like : likeReducer
	}
});