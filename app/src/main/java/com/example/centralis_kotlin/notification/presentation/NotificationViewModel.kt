    private val _notifications = MutableStateFlow(
        listOf(
            NotificationUiModel(
                id = "1",
                title = "Remote Work Policy Update",
                message = "",
                date = "May 20, 2024",
                type = NotificationType.ANNOUNCEMENT
            ),
            NotificationUiModel(
                id = "2",
                title = "New Employee Wellness",
                message = "",
                date = "May 15, 2024",
                type = NotificationType.EVENT
            ),
            NotificationUiModel(
                id = "3",
                title = "Announcement of the Next",
                message = "",
                date = "May 10, 2024",
                type = NotificationType.ANNOUNCEMENT
            )
        )
    )
    val notifications: StateFlow<List<NotificationUiModel>> = _notifications
}

