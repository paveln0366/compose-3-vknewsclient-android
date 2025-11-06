package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.vknewsclient.MainViewModel
import com.example.vknewsclient.domain.PostComment

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val feedPosts = viewModel.feedPosts.observeAsState(listOf())

    if (feedPosts.value.isNotEmpty()) {
        val comments = mutableListOf<PostComment>().apply {
            repeat(20) {
                add(
                    PostComment(id = it)
                )
            }
        }
        CommentsScreen(feedPost = feedPosts.value[0], comments = comments)
    }
//    LazyColumn(
//        modifier = Modifier.padding(paddingValues),
//        contentPadding = PaddingValues(
//            top = 16.dp,
//            start = 8.dp,
//            end = 8.dp,
//            bottom = 16.dp
//        ),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(
//            items = feedPosts.value,
//            key = { it.id }
//        ) { feedPost ->
//            val dismissState = rememberSwipeToDismissBoxState()
//            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
//                viewModel.remove(feedPost)
//            }
//            SwipeToDismissBox(
//                modifier = Modifier.animateItem(),
//                state = dismissState,
//                backgroundContent = {},
//                enableDismissFromStartToEnd = false
//            ) {
//                PostCard(
//                    feedPost = feedPost,
//                    onViewsClickListener = { statisticItem ->
//                        viewModel.updateCount(feedPost, statisticItem)
//                    },
//                    onShareClickListener = { statisticItem ->
//                        viewModel.updateCount(feedPost, statisticItem)
//                    },
//                    onCommentClickListener = { statisticItem ->
//                        viewModel.updateCount(feedPost, statisticItem)
//                    },
//                    onLikeClickListener = { statisticItem ->
//                        viewModel.updateCount(feedPost, statisticItem)
//                    }
//                )
//            }
//        }
//    }
}