package com.stafsus.waapi.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {
    companion object {
        const val INSTALL_EX = "install_ex"
        const val INSTALL_Q = "install_q"
        const val INSTALL_RK = "deploy.*.install"
        const val UNINSTALL_EX = "uninstall_ex"
        const val UNINSTALL_RK = "deploy.*.uninstall"
        const val UNINSTALL_Q = "uninstall_q"
        const val RESTART_EX = "restart_ex"
        const val RESTART_RK = "deploy.*.restart"
        const val RESTART_Q = "restart_q"
        const val LOGS_Q = "logs_q"
    }

    @Bean
    fun installQueue(): Queue {
        return Queue(INSTALL_Q, true)
    }

    @Bean
    fun installExchange(): TopicExchange {
        return TopicExchange(INSTALL_EX)
    }

    @Bean
    fun installBindings(): Declarables {
        return Declarables(
                BindingBuilder.bind(installQueue()).to(installExchange()).with(INSTALL_RK),
                BindingBuilder.bind(logsQueue()).to(installExchange()).with(INSTALL_RK)
        )
    }

    @Bean
    fun uninstallQueue(): Queue {
        return Queue(UNINSTALL_Q, true)
    }

    @Bean
    fun uninstallExchange(): TopicExchange {
        return TopicExchange(UNINSTALL_EX)
    }

    @Bean
    fun uninstallBindings(): Declarables {
        return Declarables(
                BindingBuilder.bind(uninstallQueue()).to(uninstallExchange()).with(UNINSTALL_RK),
                BindingBuilder.bind(logsQueue()).to(uninstallExchange()).with(UNINSTALL_RK)
        )
    }

    @Bean
    fun restartQueue(): Queue {
        return Queue(RESTART_Q, true)
    }

    @Bean
    fun restartExchange(): TopicExchange {
        return TopicExchange(RESTART_EX)
    }

    @Bean
    fun restartBindings(): Declarables {
        return Declarables(
                BindingBuilder.bind(restartQueue()).to(restartExchange()).with(RESTART_RK),
                BindingBuilder.bind(logsQueue()).to(restartExchange()).with(RESTART_RK)
        )
    }

    @Bean
    fun logsQueue(): Queue {
        return Queue(LOGS_Q, true)
    }


    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = producerJackson2MessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun producerJackson2MessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

}