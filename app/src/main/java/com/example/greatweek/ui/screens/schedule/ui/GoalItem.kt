package com.example.greatweek.ui.screens.schedule.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.greatweek.R
import com.example.greatweek.ui.theme.DarkTheme

@Composable
fun GoalItem(
    title: String,
    role: String,
    modifier: Modifier = Modifier,
) {
    var checked by rememberSaveable { mutableStateOf(false) }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val (rCheck, rTitle, rRole) = createRefs()
        Checkbox(
            checked = checked,
            onCheckedChange = { value -> checked = value },
            modifier = Modifier.constrainAs(rCheck) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        Text(
            text = title,
            modifier = Modifier
                .padding(top = 4.dp)
                .constrainAs(rTitle) {
                    start.linkTo(rCheck.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier
                .padding(bottom = 4.dp)
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
            )
            Text(
                text = role,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
                role = "User"
            )
        }
    }
}

@Composable
fun GoalItemPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_goal_placeholder),
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Preview
@Composable
fun GoalItemPlaceholderPreview() {
    DarkTheme {
        Surface {
            GoalItemPlaceholder()
        }
    }
}