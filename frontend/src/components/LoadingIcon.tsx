function SingleLoadingIcon({ delay = '0s' }: { delay?: string }) {
  return (
    <div
      className="bg-accent animate-custom-bounce h-4 w-4 rounded-full"
      style={{ animationDelay: delay }}
    />
  )
}
export default function LoadingIcon() {
  return (
    <div className="flex h-full items-center justify-center">
      <div className="flex gap-2">
        <SingleLoadingIcon />
        <SingleLoadingIcon delay="0.15s" />
        <SingleLoadingIcon delay="0.3s" />
      </div>
    </div>
  )
}