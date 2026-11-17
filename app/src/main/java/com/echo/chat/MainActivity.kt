package com.echo.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF080D18)
private val SurfaceDark = Color(0xFF111A2B)
private val SurfaceRaised = Color(0xFF172338)
private val Accent = Color(0xFF61D9FF)
private val TextPrimary = Color(0xFFEAF3FF)
private val TextMuted = Color(0xFF8292AA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EchoApp() }
    }
}

private data class Conversation(
    val name: String,
    val message: String,
    val time: String,
    val initials: String,
    val unread: Int = 0,
    val online: Boolean = false
)

@Composable
private fun EchoApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var activeChat by remember { mutableStateOf<Conversation?>(null) }
    val conversations = remember {
        listOf(
            Conversation("Mira Chen", "The design is looking clean ✨", "刚刚", "MC", 2, true),
            Conversation("Alex Morgan", "明天见，别忘了！", "12:42", "AM", 0, true),
            Conversation("Echo Studio", "欢迎来到 Echo。准备好开始对话了吗？", "昨天", "ES", 0),
            Conversation("Noah", "收到，我晚点回复你。", "周一", "N", 0)
        )
    }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Background,
        surface = SurfaceDark,
        primary = Accent,
        onBackground = TextPrimary,
        onSurface = TextPrimary
    )) {
        Surface(modifier = Modifier.fillMaxSize(), color = Background) {
            if (activeChat != null) {
                ChatScreen(activeChat!!, onBack = { activeChat = null })
            } else {
                Column(Modifier.fillMaxSize()) {
                    when (selectedTab) {
                        0 -> InboxScreen(conversations) { activeChat = it }
                        1 -> DiscoverScreen()
                        else -> ProfileScreen()
                    }
                    NavigationBar(
                        containerColor = Color(0xFF0D1524),
                        contentColor = TextMuted
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.ChatBubble, null) },
                            label = { Text("消息") },
                            colors = navColors()
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.Groups, null) },
                            label = { Text("发现") },
                            colors = navColors()
                        )
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Person, null) },
                            label = { Text("我的") },
                            colors = navColors()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Accent,
    selectedTextColor = Accent,
    indicatorColor = Color(0xFF19364A),
    unselectedIconColor = TextMuted,
    unselectedTextColor = TextMuted
)

@Composable
private fun InboxScreen(items: List<Conversation>, onOpen: (Conversation) -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("ECHO", color = Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
                Text("消息", color = TextPrimary, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
            Surface(shape = CircleShape, color = SurfaceRaised, modifier = Modifier.size(46.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, "新建对话", tint = Accent)
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                .background(SurfaceDark, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, null, tint = TextMuted)
            Spacer(Modifier.width(10.dp))
            Text("搜索联系人或聊天记录", color = TextMuted, fontSize = 14.sp)
        }
        Row(Modifier.padding(start = 22.dp, top = 24.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("最近对话", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            Spacer(Modifier.width(8.dp))
            Text("04", color = Accent, fontSize = 11.sp)
        }
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            items(items) { item ->
                ConversationRow(item) { onOpen(item) }
            }
        }
    }
}

@Composable
private fun ConversationRow(item: Conversation, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFF20354D), modifier = Modifier.size(54.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(item.initials, color = Accent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            if (item.online) {
                Box(Modifier.align(Alignment.BottomEnd).size(12.dp).background(Color(0xFF54E0A0), CircleShape))
            }
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(item.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.height(5.dp))
            Text(item.message, color = TextMuted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(item.time, color = TextMuted, fontSize = 11.sp)
            if (item.unread > 0) {
                Spacer(Modifier.height(7.dp))
                Box(Modifier.background(Accent, CircleShape).padding(horizontal = 7.dp, vertical = 3.dp)) {
                    Text(item.unread.toString(), color = Background, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ChatScreen(chat: Conversation, onBack: () -> Unit) {
    var draft by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf("嗨！欢迎来到 Echo 👋", "这里是第一版聊天界面。") }
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().background(SurfaceDark).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", color = Accent, fontSize = 32.sp, modifier = Modifier.clickable { onBack() })
            Spacer(Modifier.width(14.dp))
            Column {
                Text(chat.name, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Text(if (chat.online) "在线" else "Echo 联系人", color = TextMuted, fontSize = 11.sp)
            }
        }
        LazyColumn(
            Modifier.weight(1f).fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("今天", color = TextMuted, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
            items(messages) { message ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(
                        message,
                        color = TextPrimary,
                        modifier = Modifier.background(SurfaceRaised, RoundedCornerShape(16.dp)).padding(13.dp)
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().background(SurfaceDark).navigationBarsPadding().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = draft,
                onValueChange = { draft = it },
                modifier = Modifier.weight(1f).background(SurfaceRaised, RoundedCornerShape(22.dp)).padding(horizontal = 16.dp, vertical = 13.dp),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                decorationBox = { inner ->
                    if (draft.isEmpty()) Text("输入消息…", color = TextMuted, fontSize = 14.sp)
                    inner()
                }
            )
            Spacer(Modifier.width(10.dp))
            Surface(shape = CircleShape, color = Accent, modifier = Modifier.size(46.dp).clickable {
                if (draft.isNotBlank()) {
                    messages.add(draft.trim())
                    draft = ""
                }
            }) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Send, "发送", tint = Background)
                }
            }
        }
    }
}

@Composable
private fun DiscoverScreen() {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(22.dp)) {
        Text("发现", color = TextPrimary, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        FeatureCard("寻找朋友", "通过 Echo ID 找到认识的人", "即将接入")
        FeatureCard("群组空间", "和朋友们一起开启群聊", "开发计划中")
    }
}

@Composable
private fun ProfileScreen() {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(22.dp)) {
        Text("我的", color = TextPrimary, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(28.dp))
        Surface(color = SurfaceDark, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(22.dp)) {
                Text("E", color = Accent, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Echo 用户", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("账号系统将在后续版本开放", color = TextMuted, fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("版本 0.1.0 · Preview", color = TextMuted, fontSize = 12.sp)
    }
}

@Composable
private fun FeatureCard(title: String, subtitle: String, status: String) {
    Surface(color = SurfaceDark, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)) {
        Column(Modifier.padding(18.dp)) {
            Text(title, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(subtitle, color = TextMuted, fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            Text(status, color = Accent, fontSize = 11.sp)
        }
    }
}
