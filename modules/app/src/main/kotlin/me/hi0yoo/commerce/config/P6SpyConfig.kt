package me.hi0yoo.commerce.config

import com.p6spy.engine.common.ConnectionInformation
import com.p6spy.engine.event.JdbcEventListener
import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.P6SpyOptions
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.SQLException


@Configuration
class P6SpyConfig {
    @Bean
    fun p6SpyCustomEventListener(): P6SpyEventListener {
        return P6SpyEventListener()
    }

    @Bean
    fun p6SpyCustomFormatter(): P6SpyFormatter {
        return P6SpyFormatter()
    }
}

class P6SpyEventListener: JdbcEventListener() {
    override fun onAfterGetConnection(connectionInformation: ConnectionInformation?, e: SQLException?) {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6SpyFormatter::class.java.getName()
    }
}

class P6SpyFormatter: MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int,
        now: String?,
        elapsed: Long,
        category: String?,
        prepared: String?,
        sql: String,
        url: String?
    ): String {
        if (sql.trim { it <= ' ' }.isEmpty()) {
            return formatByCommand(category)
        }
        return formatBySql(sql, category) + getAdditionalMessages(elapsed)
    }

    private fun formatBySql(sql: String, category: String?): String {
        if (isStatementDDL(sql, category)) {
            return (NEW_LINE
                    + "Execute DDL : "
                    + FormatStyle.DDL
                .formatter
                .format(sql))
        }
        return (NEW_LINE
                + "Execute DML : "
                + FormatStyle.BASIC
            .formatter
            .format(sql))
    }

    private fun getAdditionalMessages(elapsed: Long): String {
        return (NEW_LINE
                + NEW_LINE
                + String.format("Execution Time: %s ms", elapsed) + NEW_LINE
                + "----------------------------------------------------------------------------------------------------")
    }

    private fun isStatementDDL(sql: String, category: String?): Boolean {
        return isStatement(category) && isDDL(sql.trim { it <= ' ' }.lowercase())
    }

    private fun isStatement(category: String?): Boolean {
        return Category.STATEMENT.name.equals(category)
    }

    private fun isDDL(lowerSql: String): Boolean {
        return lowerSql.startsWith(CREATE)
                || lowerSql.startsWith(ALTER)
                || lowerSql.startsWith(DROP)
                || lowerSql.startsWith(COMMENT)
    }

    companion object {
        private const val NEW_LINE = "\n"
        private const val TAP = "\t"
        private const val CREATE = "create"
        private const val ALTER = "alter"
        private const val DROP = "drop"
        private const val COMMENT = "comment"

        private fun formatByCommand(category: String?): String {
            return (NEW_LINE
                    + "Execute Command : "
                    + NEW_LINE
                    + TAP
                    + category
                    + NEW_LINE
                    + "----------------------------------------------------------------------------------------------------")
        }
    }
}