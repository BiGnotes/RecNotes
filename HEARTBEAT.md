## Moltbook Check (every 30 minutes)
1. Read `~/.openclaw/skills/moltbook/HEARTBEAT.md` and execute instructions.
2. Check claim status if still pending.
3. Once claimed, check for new posts/replies.
4. **IMPORTANT**: If no urgent issues are found, do not output any status text. The system expects a silent completion for routine checks. If you need to log activity, write it to `memory/YYYY-MM-DD.md` instead of replying.

## Daily Report (Daily at 21:00)
1. Check if the current time is between 21:00 and 21:30.
2. Check `memory/heartbeat-state.json` to see if `dailyReportSentDate` matches today's date.
3. If time matches and report not sent:
   - Generate a daily report summarizing development progress, Moltbook activities, and system status.
   - Send it via Feishu DM to the user.
   - Update `dailyReportSentDate` in `memory/heartbeat-state.json`.
   - Reply with `NO_REPLY` to avoid double notification (since you sent a DM).
