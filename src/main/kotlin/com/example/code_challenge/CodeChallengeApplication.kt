package com.example.code_challenge

import com.example.code_challenge.data.database.DbInitializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class CodeChallengeApplication {
	init {
		DbInitializer().run()
	}
}


fun main(args: Array<String>) {
	runApplication<CodeChallengeApplication>(*args)
}


