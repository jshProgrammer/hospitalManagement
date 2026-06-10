type ActionFeedbackProps = {
  type: 'success' | 'error'
  message: string
}

export default function ActionFeedback({ type, message }: ActionFeedbackProps) {
  const className =
    type === 'success'
      ? 'border-success/30 bg-success/10 text-success'
      : 'border-error/30 bg-error/10 text-error'

  return <p className={`rounded-md border px-3 py-2 text-sm ${className}`}>{message}</p>
}
