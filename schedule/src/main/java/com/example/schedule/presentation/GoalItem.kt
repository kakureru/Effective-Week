package com.example.schedule.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.core.ui.theme.DarkTheme

@Composable
fun GoalItem(
    title: String,
    role: String,
    onClick: () -> Unit,
    onCheck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var checked by rememberSaveable { mutableStateOf(false) }
    val shape = MaterialTheme.shapes.small
    Surface(
        modifier = modifier,
        shape = shape,
        shadowElevation = 4.dp,
        tonalElevation = 0.5.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .clip(shape)
                .clickable { onClick() }
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            val (rCheck, rTitle, rRole) = createRefs()
            Checkbox(
                checked = checked,
                onCheckedChange = { value ->
                    checked = value
                    onCheck()
                },
                modifier = Modifier.constrainAs(rCheck) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            Text(
                text = title,
                modifier = Modifier
                    .constrainAs(rTitle) {
                        start.linkTo(rCheck.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .constrainAs(rRole) {
                        start.linkTo(rTitle.start)
                        end.linkTo(parent.end)
                        top.linkTo(rTitle.bottom)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = role,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun GoalItemPreview() {
    DarkTheme {
        Surface {
            GoalItem(
                title = "Sample task",
                role = "User",
                onClick = {},
                onCheck = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun GoalItemPlaceholder(
    modifier: Modifier = Modifier,
    text: String = "New goal",
    onClick: () -> Unit,
) {
    val shape = MaterialTheme.shapes.medium
    Surface(
        modifier = modifier,
        shape = shape,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .clip(shape)
                .clickable { onClick() }
                .padding(vertical = 16.dp)
                .alpha(0.5f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun GoalItemPlaceholderPreview() {
    DarkTheme {
        Surface {
            GoalItemPlaceholder(modifier = Modifier.padding(16.dp), onClick = {})
        }
    }
}