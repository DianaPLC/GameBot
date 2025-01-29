package plc.d.gamebot.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A formatted Row object that displays [text] preceded by an optional [label].
 * Also accepts an optional [layoutRatio] float that sets the proportion of the
 * row given to the text (default 2f for a 2:1 ratio of text to label) and an
 * optional [labelColor] (default "surfaceTint"), as well as an optional standard
 * [modifier].
 */
@Composable
fun DisplayField (
    text: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    layoutRatio: Float = 2f,
    labelColor: Color = Color.Green,
    labelFont: FontFamily
) = Row (
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = modifier.padding(8.dp).fillMaxWidth()
) {
    Text(
        text = label?.uppercase() ?: "",
        color = labelColor,
        fontFamily = labelFont,
        fontWeight = FontWeight(700),
        style = MaterialTheme.typography.labelMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f)
    )
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(start = 4.dp, bottom = 2.dp)
            .weight(layoutRatio)
    )
}