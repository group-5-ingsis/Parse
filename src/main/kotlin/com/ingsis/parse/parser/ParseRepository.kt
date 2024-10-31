package com.ingsis.parse.parser

import com.ingsis.parse.Parse
import org.springframework.data.jpa.repository.JpaRepository

interface ParseRepository : JpaRepository<Parse, String>
