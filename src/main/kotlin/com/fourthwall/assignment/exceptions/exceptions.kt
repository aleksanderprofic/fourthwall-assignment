package com.fourthwall.assignment.exceptions

import java.util.UUID

class MovieNotFoundException(movieId: UUID) : RuntimeException("movie: $movieId, was not found!")
