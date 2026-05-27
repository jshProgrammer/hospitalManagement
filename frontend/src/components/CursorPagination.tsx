import Button from './Button.tsx'

type CursorPaginationProps = {
  page: number
  hasNextPage: boolean
  canGoBack: boolean
  onNextPage: () => void
  onPreviousPage: () => void
}

export default function CursorPagination({
  page,
  hasNextPage,
  canGoBack,
  onNextPage,
  onPreviousPage,
}: CursorPaginationProps) {
  return (
    <div className="border-border bg-surface flex items-center justify-between gap-4 border-t px-4 py-3">
      <div className="flex gap-2">
        <Button label="<" variant="secondary" disabled={!canGoBack} onClick={onPreviousPage} />
        <Button label={String(page + 1)} variant="primary" />
        <Button label=">" variant="secondary" disabled={!hasNextPage} onClick={onNextPage} />
      </div>

      <span className="text-muted text-sm">Page {page + 1}</span>
    </div>
  )
}
