import { type ReactNode, useEffect } from 'react'
import { createPortal } from 'react-dom'
import { X } from 'lucide-react'
import Button from './Button'

type ModalProps = {
  title: string
  children: ReactNode
  onClose: () => void
  widthClassName?: string
}

export default function Modal({
  title,
  children,
  onClose,
  widthClassName = 'max-w-4xl',
}: ModalProps) {
  useEffect(() => {
    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === 'Escape') {
        onClose()
      }
    }

    window.addEventListener('keydown', handleKeyDown)
    return () => window.removeEventListener('keydown', handleKeyDown)
  }, [onClose])

  return createPortal(
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/35 p-4">
      <button
        type="button"
        aria-label="Close modal"
        className="absolute inset-0 cursor-default"
        onClick={onClose}
      />
      <section
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-title"
        className={`border-border bg-elevated relative flex max-h-[min(46rem,calc(100vh-2rem))] w-full ${widthClassName} flex-col overflow-hidden rounded-lg border shadow-xl`}
      >
        <div className="border-border flex items-center justify-between gap-3 border-b px-5 py-4">
          <h2 id="modal-title" className="text-dark text-lg font-semibold">
            {title}
          </h2>
          <Button
            label="Close"
            variant="secondary"
            icon={<X className="size-4" />}
            onClick={onClose}
          />
        </div>
        {children}
      </section>
    </div>,
    document.body
  )
}
